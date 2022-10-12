
# 1 - Count of Records/Documents

## SQL:

```
select count(*) from person
```

## Mongo:

```
db.bios.count()
db.bios.find().count()
```
	
# 2 - Find Bios with Birth Date before 1950

## SQL:

```
select first_name, last_name, birth_date 
from person
where birth_date < date('1950-01-01')
```

## Mongo:

```
db.bios.find (
	   { birth: {$lt: ISODate("1950-01-01")} },
	   { name: 1, birth: 1 }
)
```
	
# 3 - Get a Unique Listing of all the Awards (in DB/Collection) granted

## SQL:

```
select distinct(a.award_name)
from person_awards pa, awards a
where pa.award_id = a.award_id
```

## Mongo:

```
db.bios.distinct( "awards.award" ) 
```
	
# 4 - Get a Sorted Listing of all the First Names (ascending order)

## SQL:
	
```
select first_name
from person
order by 1
```

## Mongo:

```
db.bios.find( {}, {'name.first': 1} ).sort( {'name.first': 1} )		
```

# 5 - Get a Sorted Listing of all the First Names (descending order)

## SQL:

```
select first_name
from person
order by 1 desc
```

## Mongo:

```
db.bios.find( {}, {'name.first': 1} ).sort( {'name.first': -1} )		
```
		
# 6 - Count the number of BIOS that don't yet have an award  

## SQL:

```
select count(*) from person p
where not exists 
  	(select 1 from person_awards 
   	 where person_id = p.person_id)
```

## Mongo:
	
```
db.bios.find( {awards: {$exists: false}} ).count()
db.bios.count( {awards: {$exists: false}} )
```

# 7 - Display the System ID (Primary Key) for the BIO in Query #6

## SQL:

```
select p.person_id from person p
where not exists 
	(select 1 from person_awards 
	 where person_id = p.person_id)
```

## Mongo:

```
db.bios.find( {awards: {$exists: false}}, { _id: true} )
```

# 8. Display names (first and last) along with awards and contributions from BIOS with 1 Contribution AND 2 Awards

## SQL:

```
select p.first_name, p.last_name
from person p
where (select count(*) from contribs c where c.person_id = p.person_id) = 1
and (select count(*) from person_awards pa where pa.person_id = p.person_id) = 2
```

## Mongo:


```
db.bios.find (
	{ awards: { $size: 2 }, contribs: { $size: 1 } },
	{ 'name.first': 1, 'name.last': 1 }
)
```

## SQL:

```
select p.first_name, p.last_name, c.contribution, a.award_name
from person p, person_awards pa, awards a, contribs c
where p.person_id = c.person_id
and p.person_id = pa.person_id
and pa.award_id = a.award_id
and  (select count(*) from contribs c where c.person_id = p.person_id) = 1
and (select count(*) from person_awards pa where pa.person_id = p.person_id) = 2
```

## Mongo:

```
db.bios.find (
	{ awards: { $size: 2 }, contribs: { $size: 1 } },
	{ 'name.first': 1, 'name.last': 1, 'awards': 1, 'contribs': 1 }
)
```

# 9. Display names (first and last) along with awards and contributions from BIOS with 1 Contributions OR 2 Awards

## SQL:

```
select p.first_name, p.last_name
from person p
where (select count(*) from contribs c where c.person_id = p.person_id) = 1
or (select count(*) from person_awards pa where pa.person_id = p.person_id) = 2
```

## Mongo:

```
db.bios.find (
	{ $or: [ {awards: { $size: 2 }}, {contribs: { $size: 1 }} ] },
	{ 'name.first': 1, 'name.last': 1 }
)
```

## SQL:

```
select p.first_name, p.last_name, c.contribution, a.award_name
from person p
left join contribs c using (person_id)
left join person_awards pa using (person_id)
left join awards a using (award_id)
where (select count(*) from contribs c where c.person_id = p.person_id) = 1
or (select count(*) from person_awards pa where pa.person_id = p.person_id) = 2
```

## Mongo:

```
db.bios.find (
	{ $or: [ {awards: { $size: 2 }}, {contribs: { $size: 1 }} ] },
	{ 'name.first': 1, 'name.last': 1, 'awards': 1, 'contribs': 1 }
)
```

# 10 - List all the Awards for a BIO

## SQL:

```
select p.first_name, p.last_name, a.award_name
from awards a, person_awards pa, person p
where a.award_id = pa.award_id
and p.person_id = pa.person_id
and p.person_id = 1
```

## Mongo:

```
db.bios.find (
   {  "_id" : ObjectId("543efb5060ac6af54acbe765") },
   { name: 1, awards: 1 }
)
```

	
# 11 - Display a list of Awards (names) and a count of how many of them where granted

## SQL:

```
select a.award_name as award, count(*) as granted
from person_awards pa, awards a
where pa.award_id = a.award_id
group by a.award_name
having count(*) >= 2
```

## Mongo:

```
db.bios.aggregate( [    { $unwind : "$awards" },
                        { $group: {  _id: "$awards.award", count: { $sum: 1 } } },
                        { $project : { _id: 0, award: "$_id", granted: "$count", granted_gte_2: { $gte : [ "$count", 2 ] } } },
                        { $sort: { award: 1 } },
                        { $match: { granted_gte_2: true } }
                   ] )
```


