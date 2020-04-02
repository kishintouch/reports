select count("objectName") from (select distinct "objectName" from
(select "objectName" from vfde.trails_ip) ab
except
select distinct "objectName" from
(select "objectName" from vfde.subtrails where "subType" like 'IP Consumer%') cd) a;