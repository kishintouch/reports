select count("objectName") from 
(select "objectName" from vfde.trails_atmpdh where "subType" like '%PDH Circuit%'
UNION
select "objectName" from vfde.subtrails where "subType" like '%PDH Circuit%') a;