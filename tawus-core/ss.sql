insert into daily_shift select daily_sequence.nextval, active, shift_date, version, employee_id, shift_id from a where shift_date >= '01-08-2011' and employee_id = (select id from employee where alias='BILALM')
/
