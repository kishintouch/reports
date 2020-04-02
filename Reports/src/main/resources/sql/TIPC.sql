select count("objectName") from 
(select "objectName" from vfde.trails_ip
UNION
select "objectName" from vfde.subtrails where "subType" like 'IP Consumer%') a;