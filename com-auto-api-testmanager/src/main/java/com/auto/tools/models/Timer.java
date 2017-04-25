package com.auto.tools.models;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.concurrent.TimeUnit;

public class Timer 
{
	private LocalDateTime scheduleStartTime;
	private LocalDateTime scheduleEndTime;
	private LocalTime specificTriggerTime;
	private LocalDateTime nextAlertingTime;
	private int timeSpan = 0;
	private TimeUnit timeunit;
	private boolean bySpecificTime = false;
	private boolean isTimerRunning = false;
	private boolean isStartNow = false;
	private boolean isFirstRun = true;

	public Timer(LocalDateTime startTime, LocalDateTime endTime, boolean bySpecificTime, LocalTime triggerTime, boolean startNow)
	{
		this.scheduleStartTime = startTime;
		this.scheduleEndTime = endTime;
		this.bySpecificTime = bySpecificTime;
		this.specificTriggerTime = triggerTime;
		this.isStartNow = startNow;
		this.timeunit = TimeUnit.DAYS;
		
		this.isTimerRunning = true;
	}

	public Timer(LocalDateTime startTime, LocalDateTime endTime, boolean bySpecificTime, int timeSpan, TimeUnit unit)
	{
		this.scheduleStartTime = startTime;
		this.scheduleEndTime = endTime;
		this.bySpecificTime = bySpecificTime;
		this.timeSpan = timeSpan;
		this.timeunit = unit;

		this.isTimerRunning = true;
		this.isFirstRun = true;
	}

	public LocalDateTime getNextScheduledTime()
	{
		return this.nextAlertingTime;
	}

	public void updateNextScheduledTime()
	{
		if(this.bySpecificTime)
		{
			this.nextAlertingTime = LocalDateTime.of(LocalDate.now(), specificTriggerTime);
			
			if(this.nextAlertingTime.isBefore(LocalDateTime.now()))
			{
				this.nextAlertingTime = this.nextAlertingTime.plusDays(1);
			}
		}
		else
		{
			if(this.scheduleStartTime.isAfter(LocalDateTime.now()))
			{
				this.nextAlertingTime = this.scheduleStartTime;
				isFirstRun = false;
			}
			else
			{
				this.nextAlertingTime = LocalDateTime.now();

				if(isFirstRun)
				{
					isFirstRun = false;
				}
				else if(this.timeunit == TimeUnit.DAYS)
				{
					this.nextAlertingTime = this.nextAlertingTime.plusDays(this.timeSpan);
				}
				else if(this.timeunit == TimeUnit.HOURS)
				{
					this.nextAlertingTime = this.nextAlertingTime.plusHours(this.timeSpan);
				}
				else if(this.timeunit == TimeUnit.MINUTES)
				{
					this.nextAlertingTime = this.nextAlertingTime.plusMinutes(this.timeSpan);
				}
			}
		}

		if(this.nextAlertingTime.isAfter(this.scheduleEndTime))
		{
			this.stopTimer();
		}
	}

	public void waitForNextAlerting()
	{
		System.out.println("Wait For Next Alerting");
		// For the 1st Run
		if(this.isStartNow)
		{
			this.isStartNow = false;
			return;
		}

		LocalDateTime currentDateTime = LocalDateTime.now();

		// Wait for StartTime
		while(isTimerRunning && this.scheduleStartTime.isAfter(currentDateTime))
		{
			try
			{
				if(this.timeunit == TimeUnit.MINUTES)
				{
					Thread.sleep(5000);
				}
				else
				{
					Thread.sleep(60000);
				}
			} catch (InterruptedException e) 
			{
				e.printStackTrace();
			}

			currentDateTime = LocalDateTime.now();
		}


		// Wait for Next Iteration
		while(isTimerRunning && this.nextAlertingTime.isAfter(currentDateTime))
		{
			try
			{
				if(this.timeunit == TimeUnit.MINUTES)
				{
					Thread.sleep(5000);
				}
				else
				{
					Thread.sleep(60000);
				}
			} catch (InterruptedException e) 
			{
				e.printStackTrace();
			}

			currentDateTime = LocalDateTime.now();
		}
	}

	public boolean isTimerRunning()
	{
		return this.isTimerRunning;
	}

	public void stopTimer()
	{
		this.isTimerRunning = false;
	}
}
