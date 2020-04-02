select count("objectName") from (select distinct "objectName" from
(select "objectName" from vfde.trails_atmpdh where "subType" like '%PDH Circuit%') ab
except
select distinct "objectName" from
(select "objectName" from vfde.subtrails where "subType" like '%PDH Circuit%') cd) a;