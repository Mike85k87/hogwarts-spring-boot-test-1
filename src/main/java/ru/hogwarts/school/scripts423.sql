select student.name, student.age
from student
inner join faculty on student.faculty_id = faculty.id;

select student.name, student.age
from student
inner join avatar on student.id = avatar.id;