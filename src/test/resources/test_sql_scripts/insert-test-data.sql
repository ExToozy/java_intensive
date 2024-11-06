INSERT INTO habit_tracker_schema.users (email, password, is_admin)
VALUES ('ex@mail.ru', 'uirnF4LXZpkp2lxkqs1V4Q==', false),
       ('admin', 'oI3PdKpcZHtWcMfpPwXw0w==', true),
       ('test', 'oI3PdKpcZHtWcMfpPwXw0w==', false);

INSERT INTO habit_tracker_schema.habits (user_id, name, description, frequency, day_of_creation)
VALUES (1, 'Drink water', 'Need to drink 2 liters of water every day', 'DAILY', current_date),
       (1, 'Walk 10000 steps', 'Need to walk at least 10000 steps every day', 'DAILY', current_date),
       (1, 'Training', 'Go to the gym once a week', 'WEEKLY', current_date - interval '3 weeks');

INSERT INTO habit_tracker_schema.habit_tracks (habit_id, complete_date)
VALUES (1, current_date),
       (3, current_date),
       (3, current_date - interval '1 weeks'),
       (3, current_date - interval '2 weeks');