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
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.annotation.Nonempty;
import com.helger.base.concurrent.ThreadHelper;
import com.helger.base.enforce.ValueEnforcer;
import com.helger.photon.core.job.longrun.AbstractScopeAwareLongRunningJob;
import com.helger.photon.mgrs.longrun.LongRunningJobResult;
import com.helger.photon.security.CSecurity;
import com.helger.quartz.DisallowConcurrentExecution;
import com.helger.quartz.IJobExecutionContext;
import com.helger.quartz.JobDataMap;
import com.helger.quartz.JobExecutionException;
import com.helger.quartz.SimpleScheduleBuilder;
import com.helger.quartz.TriggerKey;
import com.helger.schedule.quartz.GlobalQuartzScheduler;
import com.helger.schedule.quartz.trigger.JDK8TriggerBuilder;
import com.helger.text.IMultilingualText;
import com.helger.text.util.TextHelper;

/**
 * A dummy long running job that does nothing but sleep for {@link #DURATION_SECONDS} seconds. It
 * exists solely to fill the "Long running jobs" and the "Scheduler" application information pages
 * with data that can be looked at while the job is running.
 *
 * @author Philip Helger
 */
@DisallowConcurrentExecution
public final class DemoLongRunningJob extends AbstractScopeAwareLongRunningJob
{
  /** The number of seconds this job pretends to be busy */
  public static final int DURATION_SECONDS = 10;
  /** The job type of this job */
  public static final String JOB_TYPE = "demo";

  private static final IMultilingualText JOB_DESCRIPTION = TextHelper.create_DE_EN ("Demo Job der " +
                                                                                    DURATION_SECONDS +
                                                                                    " Sekunden lang läuft",
                                                                                    "Demo job that runs for " +
                                                                                                        DURATION_SECONDS +
                                                                                                        " seconds");
  private static final Logger LOGGER = LoggerFactory.getLogger (DemoLongRunningJob.class);

  /**
   * Public no argument constructor must be available.
   */
  public DemoLongRunningJob ()
  {}

  @Override
  @Nullable
  protected String getCurrentUserID (@NonNull final JobDataMap aJobDataMap)
  {
    // Scheduled jobs are not started by a logged in user
    return CSecurity.USER_GUEST_ID;
  }

  @NonNull
  @Nonempty
  public String getJobType ()
  {
    return JOB_TYPE;
  }

  @NonNull
  public IMultilingualText getJobDescription ()
  {
    return JOB_DESCRIPTION;
  }

  @NonNull
  public LongRunningJobResult createLongRunningJobResult ()
  {
    return LongRunningJobResult.createText ("Slept for " + DURATION_SECONDS + " seconds");
  }

  @Override
  protected void onExecute (@NonNull final JobDataMap aJobDataMap,
                            @NonNull final IJobExecutionContext aContext) throws JobExecutionException
  {
    LOGGER.info ("Demo long running job is now sleeping for " + DURATION_SECONDS + " seconds");
    ThreadHelper.sleepSeconds (DURATION_SECONDS);
  }

  /**
   * Call this method to schedule the demo long running job to run.
   *
   * @param aScheduleBuilder
   *        The schedule builder to be used. May not be <code>null</code>. Example:
   *        <code>SimpleScheduleBuilder.repeatSecondlyForever (30)</code>
   * @return The created trigger key for further usage. Never <code>null</code>.
   */
  @NonNull
  public static TriggerKey schedule (@NonNull final SimpleScheduleBuilder aScheduleBuilder)
  {
    ValueEnforcer.notNull (aScheduleBuilder, "ScheduleBuilder");

    return GlobalQuartzScheduler.getInstance ()
                                .scheduleJob (DemoLongRunningJob.class.getName (),
                                              JDK8TriggerBuilder.newTrigger ()
                                                                .startNow ()
                                                                .withSchedule (aScheduleBuilder),
                                              DemoLongRunningJob.class,
                                              null);
  }
}
