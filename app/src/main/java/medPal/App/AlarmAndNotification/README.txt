*******************************************************
**           AlarmAndNotification Package            **
*******************************************************

This package helps to create alarms to trigger notifications on specific time.

Created by Jun
Ask me if you have any problem.
Let me know if you need detailed documentation or logic behind this

-------------------------------------------------------
To create alarm and notifications
-------------------------------------------------------

First, go to your Receiver(eg AppointmentReceiver for Appointment related notif)
class to edit your Notification message.
Change the variables on the top: TITLE, TEXT, INFO

Then follow the instrcution here to learn how to use AlarmHelper to create alarm.

-------------------------------------------------------
How to use AlarmHelper
-------------------------------------------------------

First, initialize AlarmHelper:
> AlarmHelper alarmHelper = new AlarmHelper(<(Context)context>, <(int)type>);
- type:  to avoid mistakes, type integers are defined in AlarmHelper as follow
  AlarmHelper.PILL_REMINDER
  AlarmHelper.APPOINTMENT
  AlarmHelper.BLOOD_PRESSURE
  AlarmHelper.BLOOD_SUGAR
  (Use these defined constant to avoid mistakes)


To create ONE-TIME alarm (not repeating):
> alarmHelper.setAlarm(<(long)alarmTime>, <(int)reference_id>);
- alarmTime: the time (in milli) to trigger alarm
  (Note: in some cases you can use TimeAndInterval class to help you in
  converting time to milli)
- reference_id: reference for the item(pill reminder/appointment etc) related
  to this alarm, so that it is easier to delete the alarm.

To create REPEATING alarm
(Note: for alarms that repeat on specific days of week(eg. every Mon and Thur)
read next section.)
> alarmHelper.setAlarm(<(long)firstAlarmTime>, <(long)repeatInterval>, <(int)reference_id>);
- firstAlarmTime: the first time (in milli also) that the alarm should be triggered
- repeatInterval: repeat interval in milli (again TimeAndInterval class might help)
- reference_id: see above

To create REPEAT ON DAYS OF WEEK alarm
> alarmHelper.setAlarm(<(long)firstAlarmTime>, <(String)daysOfWeek>, <(int)reference_id>);
- firstAlarmTime: see above
- daysOfWeek: "1001011" means repeat on every Monday, Thursday, Saturday and Sunday
- reference_id: see above

NOTE: if you need other kind of repeating alarm and you think it's better to
      implement here, just let me know if i can help (you can try to implement
      yourself first)


To delete ALL alarm of the same reference_id:
(eg. when user delete a pill reminder, this will help to remove all
alarm related to that pill reminder)
> alarmHelper.cancelAlarm(<(int)reference_id>);

To delete A SPECIFIC TIME AND INTERVAL alarm
> alarmHelper.cancelAlarm(<(int)reference_id>, <(long)time>, <(long)interval>);

To delete DAYS OF WEEK alarm
> alarmHelper.cancelAlarm(<(long)firstAlarmTime>, <(String)daysOfWeek>, <(int)reference_id>);

NOTE: Let me know if you have any problem or you have other use case that this
      class does not cover, i'll see if i can implement that
