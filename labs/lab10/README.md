
# CMPE 172 - Lab #10 - DevOps CI/CD

Lab Files with Starter Code: https://github.com/paulnguyen/cmpe172/tree/main/labs/lab10

In this lab, you will use the starter code from:  https://github.com/paulnguyen/cmpe172/tree/main/labs/lab10/starter-code/spring-gumball and create a repo named *spring-gumball* in your personal GitHub Report (not your private repo assigned for the class).

Once you have this repo in place, add the following to GitHub Workflows via GitHub Actions.


## CI Workflow

* https://help.github.com/actions/language-and-framework-guides/building-and-testing-java-with-gradle

![github-action-gradle-build.png](images/github-action-gradle-build.png)

Set up your workflow to trigger on *push* and *pr* on *main* branch and optionally upload build artifact (i.e. jar file).  For example:

```

name: Java CI with Gradle

on:
  push:
    branches: [ main ]
  pull_request:
    branches: [ main ]

jobs:
  build:

    runs-on: ubuntu-latest

    steps:
    - uses: actions/checkout@v2
    - name: Set up JDK 11
      uses: actions/setup-java@v2
      with:
        java-version: '11'
        distribution: 'adopt'
    - name: Grant execute permission for gradlew
      run: chmod +x gradlew
    - name: Build with Gradle
      run: ./gradlew build
    - name: Build Result
      run: ls build/libs
    - name: Upload a Build Artifact
      uses: actions/upload-artifact@v2.2.3
      with:
        name: spring-gumball
        path: build/libs/spring-gumball-2.0.jar
```

Make a change to the code and commit to *main* branch to trigger the action.  Take screenhots of your result.

![github-action-build-resuts.png](images/github-action-build-resuts.png)


## CD Workflow

* https://github.com/google-github-actions/setup-gcloud/tree/master/example-workflows/gke
* https://cloud.google.com/iam/docs/creating-managing-service-accounts
* https://kustomize.io

![github-action-deploy-gke.png](images/github-action-deploy-gke.png)

Do the following to configure and launch a new GKE cluster in preparation for the CD Deployment.

1. Ensure that your repository contains the necessary configuration for your Google Kubernetes Engine cluster, including deployment.yml, kustomization.yml, service.yml, etc.

* kustomization.yml

```
apiVersion: kustomize.config.k8s.io/v1beta1
kind: Kustomization
resources:
- deployment.yaml
- service.yaml
```

* deployment.yaml

```
apiVersion: apps/v1
kind: Deployment
metadata:
  name: spring-gumball-deployment
  namespace: default
spec:
  selector:
    matchLabels:
      name: spring-gumball
  replicas: 4 # tells deployment to run 2 pods matching the template
  template: # create pods using pod definition in this template
    metadata:
      # unlike pod.yaml, the name is not included in the meta data as a unique name is
      # generated from the deployment name
      labels:
        name: spring-gumball
    spec:
      containers:
      - name: spring-gumball
        image: gcr.io/PROJECT_ID/IMAGE:TAG
        ports:
        - containerPort: 8080
```

* service.yaml

```
apiVersion: v1
kind: Service
metadata:
  name: spring-gumball-service 
  namespace: default
spec:
  type: NodePort
  ports:
  - port: 8080
    targetPort: 8080 
  selector:
    name: spring-gumball
```


2. Set up secrets in your workspace: GKE_PROJECT with the name of the project and GKE_SA_KEY with the Base64 encoded JSON service account key (https://github.com/GoogleCloudPlatform/github-actions/tree/docs/service-account-key/setup-gcloud#inputs).

* GCP Service Accoucnt & JSON Service Account Key

![service-account.png](images/service-account.png)
![service-account-key.png](images/service-account-key.png)

* GitHub Action Secrets

![github-action-secrets.png](images/github-action-secrets.png)



3. Change the values for the GKE_ZONE, GKE_CLUSTER, IMAGE, and DEPLOYMENT_NAME environment variables (see example below).

```
name: Build and Deploy to GKE

on:
  release:
    types: [created]

env:
  PROJECT_ID: ${{ secrets.GKE_PROJECT }}
  GKE_CLUSTER: cmpe172    # TODO: update to cluster name
  GKE_ZONE: us-central1-c   # TODO: update to cluster zone
  DEPLOYMENT_NAME: spring-gumball # TODO: update to deployment name
  IMAGE: spring-gumball

jobs:
  setup-build-publish-deploy:
    name: Setup, Build, Publish, and Deploy
    runs-on: ubuntu-latest
    environment: production

    steps:
    - name: Checkout
      uses: actions/checkout@v2

    # Build JAR File
    - name: Set up JDK 11
      uses: actions/setup-java@v2
      with:
        java-version: '11'
        distribution: 'adopt'
    - name: Grant execute permission for gradlew
      run: chmod +x gradlew
    - name: Build with Gradle
      run: ./gradlew build
    - name: Build Result
      run: ls build/libs

    # Setup gcloud CLI
    - uses: google-github-actions/setup-gcloud@v0.2.0
      with:
        service_account_key: ${{ secrets.GKE_SA_KEY }}
        project_id: ${{ secrets.GKE_PROJECT }}

    # Configure Docker to use the gcloud command-line tool as a credential
    # helper for authentication
    - run: |-
        gcloud --quiet auth configure-docker

    # Get the GKE credentials so we can deploy to the cluster
    - uses: google-github-actions/get-gke-credentials@v0.2.1
      with:
        cluster_name: ${{ env.GKE_CLUSTER }}
        location: ${{ env.GKE_ZONE }}
        credentials: ${{ secrets.GKE_SA_KEY }}

    # Build the Docker image
    - name: Build
      run: |-
        docker build \
          --tag "gcr.io/$PROJECT_ID/$IMAGE:$GITHUB_SHA" \
          --build-arg GITHUB_SHA="$GITHUB_SHA" \
          --build-arg GITHUB_REF="$GITHUB_REF" \
          .

    # Push the Docker image to Google Container Registry
    - name: Publish
      run: |-
        docker push "gcr.io/$PROJECT_ID/$IMAGE:$GITHUB_SHA"

    # Set up kustomize
    - name: Set up Kustomize
      run: |-
        curl -sfLo kustomize https://github.com/kubernetes-sigs/kustomize/releases/download/v3.1.0/kustomize_3.1.0_linux_amd64
        chmod u+x ./kustomize

    # Deploy the Docker image to the GKE cluster
    - name: Deploy
      run: |-
        ./kustomize edit set image gcr.io/PROJECT_ID/IMAGE:TAG=gcr.io/$PROJECT_ID/$IMAGE:$GITHUB_SHA
        ./kustomize build . | kubectl apply -f -
        #kubectl rollout status deployment/$DEPLOYMENT_NAME
        #kubectl get services -o wide
```

4. Trigger a CD Deployment by creating a new GitHub Release

* Note:  
	* Confirm the Pods and Service have been Deployed to your GKE Cluster
	* Set up a External Facing Load Balancer and Test the Gumball Spring App
	* Web UI should come up on Load Balancer's External IP (as follows)


![deploy1.png](images/deploy1.png)
![deploy2.png](images/deploy2.png)
![deploy3.png](images/deploy3.png)
![deploy4.png](images/deploy4.png)
![deploy5.png](images/deploy5.png)
![deploy6.png](images/deploy6.png)
![deploy7.png](images/deploy7.png)
![deploy8.png](images/deploy8.png)
![deploy9.png](images/deploy9.png)
![deploy10.png](images/deploy10.png)
![deploy11.png](images/deploy11.png)
![deploy12.png](images/deploy12.png)
![deploy13.png](images/deploy13.png)










