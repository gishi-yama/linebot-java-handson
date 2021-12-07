```sql
insert into reminder_item (user_id, push_at, push_text)
    values ('ABCDEFG...', '13:15', '授業開始');
insert into reminder_item (user_id, push_at, push_text)
    values ('ABCDEFG...', '16:30', '授業終了');
```

```sql
select * from reminder_item;
```