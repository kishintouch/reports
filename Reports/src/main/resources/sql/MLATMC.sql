select count("objectName") from (select distinct "objectName" from
(select "objectName" from vfde.subtrails where "subType" like '%ATM PVC%') ab) a;