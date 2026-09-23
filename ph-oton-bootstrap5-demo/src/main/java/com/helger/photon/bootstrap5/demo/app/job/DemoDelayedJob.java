/*
 * Copyright (C) 2025-2026 Philip Helger (www.helger.com)
 * philip[at]helger[dot]com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.helger.photon.bootstrap5.demo.app.job;

import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.annotation.Nonnegative;
import com.helger.base.enforce.ValueEnforcer;
import com.helger.datetime.helper.PDTFactory;
import com.helger.quartz.IJobExecutionContext;
import com.helger.quartz.JobDataMap;
import com.helger.quartz.JobExecutionException;
import com.helger.quartz.TriggerKey;
import com.helger.schedule.quartz.GlobalQuartzScheduler;
import com.helger.schedule.quartz.trigger.JDK8TriggerBuilder;
import com.helger.web.scope.util.AbstractScopeAwareJob;

/**
 * A dummy job that runs exactly once, {@link #DELAY_MINUTES} minutes after the application was
 * started. In contrast to {@link DemoLongRunningJob} it is a plain job and not an
 * {@link com.helger.photon.mgrs.longrun.ILongRunningJob}, so it only shows up on the "Scheduler"
 * application information page and not on the "Long running jobs" page.
 * <p>
 * Until it fires, its trigger start time lies in the future - which is what the "Scheduler" page
 * marks with a red badge.
 * </p>
 *
 * @author Philip Helger
 */
public final class DemoDelayedJob extends AbstractScopeAwareJob
{
  /** The number of minutes after application startup at which this job runs */
  public static final int DELAY_MINUTES = 10;

  private static final Logger LOGGER = LoggerFactory.getLogger (DemoDelayedJob.class);

  /**
   * Public no argument constructor must be available.
   */
  public DemoDelayedJob ()
  {}

  @Override
  protected void onExecute (@NonNull final JobDataMap aJobDataMap,
                            @NonNull final IJobExecutionContext aContext) throws JobExecutionException
  {
    LOGGER.info ("Demo delayed job is running, " + DELAY_MINUTES + " minutes after application startup");
  }

  /**
   * Call this method to schedule the demo delayed job to run once, after the provided number of
   * minutes.
   *
   * @param nDelayMinutes
   *        The number of minutes to wait before the job is executed. Must be &ge; 0.
   * @return The created trigger key for further usage. Never <code>null</code>.
   */
  @NonNull
  public static TriggerKey schedule (@Nonnegative final int nDelayMinutes)
  {
    ValueEnforcer.isGE0 (nDelayMinutes, "DelayMinutes");

    // No schedule builder means SimpleScheduleBuilder.simpleSchedule () - so the job fires exactly
    // once at the provided start time
    return GlobalQuartzScheduler.getInstance ()
                                .scheduleJob (DemoDelayedJob.class.getName (),
                                              JDK8TriggerBuilder.newTrigger ()
                                                                .startAt (PDTFactory.getCurrentLocalDateTime ()
                                                                                    .plusMinutes (nDelayMinutes)),
                                              DemoDelayedJob.class,
                                              null);
  }
}
