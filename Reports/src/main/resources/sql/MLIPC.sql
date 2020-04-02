select count("objectName") from (select distinct "objectName" from
(select "objectName" from vfde.subtrails where "subType" like 'IP Consumer%') ab) a;