select t2.col1 as t2_col1,
t1.col2 as t1_col2,
t1.col3 as t1_col3
from table1 t1 join table2 t2 on t1.col1 = t2.col1
where t1.col2='foo' and t2.col1=5 and t1.col3 is not null
group by rollup(t2_col1, t1_col2, 3)
order by 1 asc, 2 desc, t1_col3 
limit 100
