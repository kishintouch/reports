select count("objectName") from (select distinct "objectName" from
(select "objectName" from vfde.trails_atmpdh where "subType" like '%ATM PVC%') ab
except
select distinct "objectName" from
(select "objectName" from vfde.subtrails where "subType" like '%ATM PVC%') cd) a;