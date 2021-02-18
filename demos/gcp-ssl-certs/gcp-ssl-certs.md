## Using Google-managed SSL certificates

* https://cloud.google.com/load-balancing/docs/ssl-certificates/google-managed-certs
* https://cloud.google.com/load-balancing/docs/ssl-certificates/troubleshooting
* https://cloud.google.com/kuberun/docs/managed-tls


gcloud beta compute ssl-certificates \
  describe gumball-nguyenresearch-com --global \
  --format="get(name,managed.status)"

gcloud beta compute ssl-certificates \
  delete gumball-nguyenresearch-com --global

