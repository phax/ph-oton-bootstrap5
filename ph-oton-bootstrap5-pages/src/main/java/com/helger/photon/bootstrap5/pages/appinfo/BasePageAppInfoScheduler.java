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
package com.helger.photon.bootstrap5.pages.appinfo;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import com.helger.annotation.Nonempty;
import com.helger.annotation.misc.Translatable;
import com.helger.base.string.StringHelper;
import com.helger.collection.commons.CommonsArrayList;
import com.helger.collection.commons.ICommonsList;
import com.helger.collection.commons.ICommonsSet;
import com.helger.datetime.format.PDTToString;
import com.helger.datetime.helper.PDTFactory;
import com.helger.datetime.util.PDTDisplayHelper;
import com.helger.datetime.util.PDTDisplayHelper.IPeriodTextProvider;
import com.helger.html.hc.IHCNode;
import com.helger.html.hc.ext.HCExtHelper;
import com.helger.html.hc.html.grouping.HCLI;
import com.helger.html.hc.html.grouping.HCOL;
import com.helger.html.hc.html.grouping.HCPre;
import com.helger.html.hc.html.grouping.HCUL;
import com.helger.html.hc.html.tabular.HCCol;
import com.helger.html.hc.html.tabular.HCTable;
import com.helger.html.hc.impl.HCNodeList;
import com.helger.html.hc.impl.HCTextNode;
import com.helger.photon.bootstrap5.badge.BootstrapBadge;
import com.helger.photon.bootstrap5.badge.EBootstrapBadgeType;
import com.helger.photon.bootstrap5.buttongroup.BootstrapButtonToolbar;
import com.helger.photon.bootstrap5.form.BootstrapFormGroup;
import com.helger.photon.bootstrap5.form.BootstrapViewForm;
import com.helger.photon.bootstrap5.nav.BootstrapTabBox;
import com.helger.photon.bootstrap5.pages.AbstractBootstrapWebPage;
import com.helger.photon.bootstrap5.table.BootstrapTable;
import com.helger.photon.core.EPhotonCoreText;
import com.helger.photon.uicore.icon.EDefaultIcon;
import com.helger.photon.uicore.page.EWebPageText;
import com.helger.photon.uicore.page.IWebPageExecutionContext;
import com.helger.quartz.IJob;
import com.helger.quartz.IJobDetail;
import com.helger.quartz.IJobExecutionContext;
import com.helger.quartz.IJobListener;
import com.helger.quartz.IScheduler;
import com.helger.quartz.ITrigger;
import com.helger.quartz.ITrigger.ETriggerState;
import com.helger.quartz.JobKey;
import com.helger.quartz.SchedulerContext;
import com.helger.quartz.SchedulerException;
import com.helger.quartz.SchedulerMetaData;
import com.helger.quartz.TriggerKey;
import com.helger.quartz.impl.matchers.GroupMatcher;
import com.helger.schedule.quartz.ESchedulerState;
import com.helger.schedule.quartz.QuartzSchedulerHelper;
import com.helger.schedule.quartz.listener.JobExecutionError;
import com.helger.schedule.quartz.listener.JobExecutionErrorRegistry;
import com.helger.schedule.quartz.listener.StatisticsJobListener;
import com.helger.statistics.api.IStatisticsHandlerTimer;
import com.helger.statistics.impl.StatisticsManager;
import com.helger.text.IMultilingualText;
import com.helger.text.display.IHasDisplayTextWithArgs;
import com.helger.text.resolve.DefaultTextResolver;
import com.helger.text.util.TextHelper;

/**
 * Show all registered scheduled actions.
 *
 * @author Philip Helger
 * @param <WPECTYPE>
 *        Web Page Execution Context type
 */
public class BasePageAppInfoScheduler <WPECTYPE extends IWebPageExecutionContext> extends
                                      AbstractBootstrapWebPage <WPECTYPE>
{
  @Translatable
  protected enum EText implements IHasDisplayTextWithArgs
  {
    MSG_SCHEDULER_CONTEXT ("Kontext", "Context"),
    MSG_SUMMARY ("Zusammenfassung", "Summary"),
    MSG_SCHEDULER_STATE ("Scheduler-Status", "Scheduler state"),
    MSG_SCHEDULER_STATE_RUNNING ("Läuft", "Running"),
    MSG_SCHEDULER_STATE_STANDBY ("Standby", "Standby"),
    MSG_SCHEDULER_STATE_SHUTDOWN ("Beendet", "Shut down"),
    MSG_SCHEDULER_STATE_NOT_STARTED ("Nicht gestartet", "Not started"),
    MSG_SCHEDULER_STANDBY_HINT ("Der Scheduler ist im Standby-Modus - es werden keine Jobs ausgeführt!",
                                "The scheduler is in standby mode - no jobs are executed at all!"),
    MSG_SCHEDULER_SHUTDOWN_HINT ("Der Scheduler wurde beendet - es werden keine Jobs mehr ausgeführt!",
                                 "The scheduler was shut down - no jobs are executed any more!"),
    MSG_SCHEDULER_NOT_STARTED_HINT ("Der Scheduler wurde nie gestartet - es werden keine Jobs ausgeführt!",
                                    "The scheduler was never started - no jobs are executed at all!"),
    MSG_RUNNING_SINCE ("Läuft seit", "Running since"),
    MSG_THREAD_POOL ("Thread Pool", "Thread pool"),
    MSG_THREAD_POOL_USAGE ("{0} von {1} Threads in Verwendung", "{0} of {1} threads in use"),
    MSG_THREAD_POOL_EXHAUSTED ("Der Thread Pool ist voll ausgelastet - weitere Jobs müssen warten und können Aussetzer verursachen!",
                               "The thread pool is fully utilized - further jobs have to wait and may misfire!"),
    MSG_PAUSED_TRIGGER_GROUPS ("Pausierte Trigger-Gruppen", "Paused trigger groups"),
    MSG_PAUSED_TRIGGER_GROUPS_HINT ("Trigger in diesen Gruppen lösen nicht aus, bis die Gruppe fortgesetzt wird.",
                                    "Triggers in these groups do not fire until the group is resumed."),
    MSG_CALENDARS ("Registrierte Kalender", "Registered calendars"),
    MSG_JOBS_WITHOUT_TRIGGER ("Jobs ohne Trigger", "Jobs without a trigger"),
    MSG_JOBS_WITHOUT_TRIGGER_HINT ("Diese Jobs sind registriert, werden aber nie ausgeführt, weil sie keinen Trigger haben.",
                                   "These jobs are registered but are never executed, because they have no trigger."),
    MSG_EXECUTING_JOBS ("Laufende Jobs", "Currently executing jobs"),
    MSG_LISTENERS ("Job Listener", "Job listeners"),
    MSG_JOB_CLASS ("Job: ", "Job: "),
    MSG_TRIGGER_CLASS ("Trigger: ", "Trigger: "),
    MSG_TRIGGER_KEY ("Trigger Key: ", "trigger Key: "),
    MSG_START_TIME ("Startzeit: ", "Start time: "),
    MSG_END_TIME ("Endzeit: ", "End time: "),
    MSG_PREVIOUS_FIRE_TIME ("Letzter Aufruf: ", "Previous fire time: "),
    MSG_NEXT_FIRE_TIME ("Nächster Aufruf: ", "Next fire time: "),
    MSG_FIRE_TIME ("Auslösezeit: ", "Fire time: "),
    MSG_SCHEDULED_FIRE_TIME ("Geplante Auslösezeit: ", "Scheduled fire time: "),
    MSG_RUNNING_FOR ("Bisherige Laufzeit: ", "Running for: "),
    MSG_REFIRE_COUNT ("Anzahl Wiederholungen: ", "Refire count: "),
    MSG_RECOVERING ("Wiederherstellung: ", "Recovering: "),
    MSG_FIRE_INSTANCE_ID ("Ausführungs-ID: ", "Fire instance ID: "),
    MSG_MISFIRE_INSTRUCTIONS ("Verhalten bei Aussetzern", "Misfire instructions: "),
    MSG_TRIGGER_STATE ("Trigger-Status: ", "Trigger state: "),
    MSG_CALENDAR_NAME ("Kalender: ", "Calendar: "),
    MSG_MAY_FIRE_AGAIN ("Kann erneut auslösen: ", "May fire again: "),
    MSG_FINAL_FIRE_TIME ("Letzte Auslösung: ", "Final fire time: "),
    MSG_NEXT_FIRE_TIMES ("Die nächsten Auslösezeiten: ", "Next fire times: "),
    MSG_EXECUTION_COUNT ("Ausführungen: ", "Executions: "),
    MSG_ERROR_COUNT ("Fehler: ", "Errors: "),
    MSG_VETOED_COUNT ("Abgelehnt: ", "Vetoed: "),
    MSG_RUNTIME ("Laufzeit: ", "Runtime: "),
    MSG_RUNTIME_VALUES ("min. {0} ms, \u00f8 {1} ms, max. {2} ms", "min {0} ms, avg {1} ms, max {2} ms"),
    MSG_LAST_ERRORS ("Die letzten Fehler: ", "Most recent errors: "),
    MSG_JOB_DATA ("JobData: ", "JobData: "),
    MSG_TIME_IN ("in {0}", "in {0}"),
    MSG_TIME_AGO ("vor {0}", "{0} ago"),
    MSG_NONE ("keine", "none"),
    MSG_NOTHING_SCHEDULED ("Es sind keine Tasks geplant", "No actions are scheduled");

    @NonNull
    private final IMultilingualText m_aTP;

    EText (@NonNull final String sDE, @NonNull final String sEN)
    {
      m_aTP = TextHelper.create_DE_EN (sDE, sEN);
    }

    @Nullable
    public String getDisplayText (@NonNull final Locale aContentLocale)
    {
      return DefaultTextResolver.getTextStatic (this, m_aTP, aContentLocale);
    }
  }

  /**
   * On which side of "now" a date time is expected to be.
   */
  private enum EDateTimeExpectation
  {
    /** The date time is expected to be &le; now - as e.g. the previous fire time */
    PAST,
    /** The date time is expected to be &ge; now - as e.g. the next fire time */
    FUTURE,
    /** Both sides of "now" are equally valid - as e.g. the final fire time */
    ANY
  }

  /** The number of upcoming fire times to be shown per trigger */
  public static final int NEXT_FIRE_TIMES_COUNT = 5;

  public BasePageAppInfoScheduler (@NonNull @Nonempty final String sID)
  {
    super (sID, EWebPageText.PAGE_NAME_APPINFO_SCHEDULER.getAsMLT ());
  }

  public BasePageAppInfoScheduler (@NonNull @Nonempty final String sID, @NonNull final String sName)
  {
    super (sID, sName);
  }

  public BasePageAppInfoScheduler (@NonNull @Nonempty final String sID,
                                   @NonNull final String sName,
                                   @Nullable final String sDescription)
  {
    super (sID, sName, sDescription);
  }

  public BasePageAppInfoScheduler (@NonNull @Nonempty final String sID,
                                   @NonNull final IMultilingualText aName,
                                   @Nullable final IMultilingualText aDescription)
  {
    super (sID, aName, aDescription);
  }

  /**
   * Create a badge that shows the distance of a date time to "now". If the date time is on the
   * expected side of "now" the badge is green, otherwise it is red.
   *
   * @param aDateTime
   *        The date time to be evaluated. May not be <code>null</code>.
   * @param eExpectation
   *        On which side of "now" the date time is expected to be. May not be <code>null</code>.
   *        {@link EDateTimeExpectation#ANY} always creates a neutral badge.
   * @param aNow
   *        The current date time all values are compared to. May not be <code>null</code>.
   * @param aDisplayLocale
   *        The display locale to be used. May not be <code>null</code>.
   * @return The created badge. Never <code>null</code>.
   */
  @NonNull
  private BootstrapBadge _getRelativeTimeBadge (@NonNull final LocalDateTime aDateTime,
                                                @NonNull final EDateTimeExpectation eExpectation,
                                                @NonNull final LocalDateTime aNow,
                                                @NonNull final Locale aDisplayLocale)
  {
    final boolean bIsInFuture = aDateTime.isAfter (aNow);

    final IPeriodTextProvider aTextProvider = "de".equals (aDisplayLocale.getLanguage ()) ? IPeriodTextProvider.DE
                                                                                          : IPeriodTextProvider.EN;
    final String sPeriod = bIsInFuture ? PDTDisplayHelper.getPeriodText (aNow, aDateTime, aTextProvider)
                                       : PDTDisplayHelper.getPeriodText (aDateTime, aNow, aTextProvider);
    final String sText = bIsInFuture ? EText.MSG_TIME_IN.getDisplayTextWithArgs (aDisplayLocale, sPeriod)
                                     : EText.MSG_TIME_AGO.getDisplayTextWithArgs (aDisplayLocale, sPeriod);

    if (eExpectation == EDateTimeExpectation.ANY)
      return new BootstrapBadge (EBootstrapBadgeType.SECONDARY).addChild (sText);

    // "now" itself fulfills both ">= now" and "<= now"
    final boolean bIsAsExpected = eExpectation == EDateTimeExpectation.FUTURE ? !aDateTime.isBefore (aNow)
                                                                              : !aDateTime.isAfter (aNow);
    return bIsAsExpected ? badgeSuccess (sText) : badgeDanger (sText);
  }

  /**
   * Create the UI representation of a single labeled date time, including the badge that shows the
   * distance to "now".
   *
   * @param sLabel
   *        The label to be put in front. May not be <code>null</code>.
   * @param aDateTime
   *        The date time to be shown. May be <code>null</code>.
   * @param eExpectation
   *        On which side of "now" the date time is expected to be. May not be <code>null</code>.
   * @param aNow
   *        The current date time all values are compared to. May not be <code>null</code>.
   * @param aDisplayLocale
   *        The display locale to be used. May not be <code>null</code>.
   * @return The created node. Never <code>null</code>.
   */
  @NonNull
  private IHCNode _getDateTimeUI (@NonNull final String sLabel,
                                  @Nullable final LocalDateTime aDateTime,
                                  @NonNull final EDateTimeExpectation eExpectation,
                                  @NonNull final LocalDateTime aNow,
                                  @NonNull final Locale aDisplayLocale)
  {
    final HCNodeList ret = new HCNodeList ().addChild (sLabel);
    if (aDateTime == null)
      ret.addChild (em (EText.MSG_NONE.getDisplayText (aDisplayLocale)));
    else
      ret.addChild (PDTToString.getAsString (aDateTime, aDisplayLocale))
         .addChild (" ")
         .addChild (_getRelativeTimeBadge (aDateTime, eExpectation, aNow, aDisplayLocale));
    return ret;
  }

  /**
   * Determine the state of a single scheduler.
   * <p>
   * {@link QuartzSchedulerHelper#getSchedulerState()} cannot be used here, because it always works
   * on the default scheduler while this page iterates all of them. Note that
   * <code>IScheduler.isStarted ()</code> is not usable either, because <code>StdScheduler</code>
   * implements it as "was ever started" and therefore returns <code>true</code> in standby mode as
   * well - only the start date tells standby and "never started" apart.
   * </p>
   *
   * @param aScheduler
   *        The scheduler to be evaluated. May not be <code>null</code>.
   * @param aMetaData
   *        The metadata of that scheduler. May not be <code>null</code>.
   * @return The determined state. Never <code>null</code>.
   * @throws SchedulerException
   *         If querying the scheduler fails
   */
  @NonNull
  private static ESchedulerState _getSchedulerState (@NonNull final IScheduler aScheduler,
                                                     @NonNull final SchedulerMetaData aMetaData) throws SchedulerException
  {
    if (aScheduler.isShutdown ())
      return ESchedulerState.SHUTDOWN;
    if (aScheduler.isInStandbyMode ())
      return aMetaData.getRunningSince () == null ? ESchedulerState.NOT_STARTED : ESchedulerState.STANDBY;
    return ESchedulerState.STARTED;
  }

  /**
   * Create the badge for the state of a single trigger. Only {@link ETriggerState#NORMAL} is a
   * healthy state.
   *
   * @param eState
   *        The trigger state to be displayed. May not be <code>null</code>.
   * @return The created badge. Never <code>null</code>.
   */
  @NonNull
  private BootstrapBadge _getTriggerStateBadge (@NonNull final ETriggerState eState)
  {
    return switch (eState)
    {
      case NORMAL -> /* The only state in which the trigger fires as configured */ badgeSuccess (eState.name ());
      case ERROR -> /* The trigger is broken and will never fire again */ badgeDanger (eState.name ());
      case PAUSED, BLOCKED -> /*
                               * Temporarily not firing - may resolve itself (BLOCKED) or not
                               * (PAUSED)
                               */ badgeWarn (eState.name ());
      default -> /* COMPLETE and NONE - nothing more to expect, but nothing broken either */ new BootstrapBadge (EBootstrapBadgeType.SECONDARY).addChild (eState.name ());
    };
  }

  /**
   * Create the UI representation of the upcoming fire times of a single trigger. This is the
   * easiest way to verify that e.g. a cron expression means what it is supposed to mean.
   *
   * @param aTrigger
   *        The trigger to be evaluated. May not be <code>null</code>.
   * @param aDisplayLocale
   *        The display locale to be used. May not be <code>null</code>.
   * @return <code>null</code> if the trigger has no upcoming fire time at all.
   */
  @Nullable
  private IHCNode _getNextFireTimesUI (@NonNull final ITrigger aTrigger, @NonNull final Locale aDisplayLocale)
  {
    Date aFireTime = aTrigger.getNextFireTime ();
    if (aFireTime == null)
      return null;

    final HCOL aOL = new HCOL ();
    for (int i = 0; i < NEXT_FIRE_TIMES_COUNT && aFireTime != null; ++i)
    {
      aOL.addItem (PDTToString.getAsString (PDTFactory.createLocalDateTime (aFireTime), aDisplayLocale));
      aFireTime = aTrigger.getFireTimeAfter (aFireTime);
    }
    return new HCNodeList ().addChild (EText.MSG_NEXT_FIRE_TIMES.getDisplayText (aDisplayLocale)).addChild (aOL);
  }

  /**
   * Get the value of a single statistics counter, without creating the counter as a side effect.
   *
   * @param aAllCounterNames
   *        All currently known counter names. May not be <code>null</code>.
   * @param sName
   *        The name of the counter to be read. May not be <code>null</code>.
   * @return 0 if no such counter exists.
   */
  private static long _getCounterValue (@NonNull final Set <String> aAllCounterNames, @NonNull final String sName)
  {
    // StatisticsManager.getCounterHandler creates the handler on demand, so the existence must be
    // checked beforehand - otherwise merely looking at this page creates empty counters
    return aAllCounterNames.contains (sName) ? StatisticsManager.getCounterHandler (sName).getCount () : 0;
  }

  /**
   * Create the UI representation of the execution statistics of a single job, as they are collected
   * by the <code>StatisticsJobListener</code> that <code>GlobalQuartzScheduler</code> registers by
   * default.
   *
   * @param aJobClass
   *        The job class to get the statistics for. May not be <code>null</code>.
   * @param aDisplayLocale
   *        The display locale to be used. May not be <code>null</code>.
   * @return <code>null</code> if no statistics were collected for that job class so far.
   */
  @Nullable
  private IHCNode _getJobStatisticsUI (@NonNull final Class <? extends IJob> aJobClass,
                                       @NonNull final Locale aDisplayLocale)
  {
    final String sStatsName = StatisticsJobListener.getStatisticsName (aJobClass);
    final ICommonsSet <String> aAllCounterNames = StatisticsManager.getAllCounterHandler ();
    if (!aAllCounterNames.contains (sStatsName + StatisticsJobListener.STATS_SUFFIX_EXEC) &&
        !aAllCounterNames.contains (sStatsName + StatisticsJobListener.STATS_SUFFIX_ERROR) &&
        !aAllCounterNames.contains (sStatsName + StatisticsJobListener.STATS_SUFFIX_VETOED))
    {
      // The job was never executed so far
      return null;
    }

    final long nExecutions = _getCounterValue (aAllCounterNames, sStatsName + StatisticsJobListener.STATS_SUFFIX_EXEC);
    final long nErrors = _getCounterValue (aAllCounterNames, sStatsName + StatisticsJobListener.STATS_SUFFIX_ERROR);
    final long nVetoed = _getCounterValue (aAllCounterNames, sStatsName + StatisticsJobListener.STATS_SUFFIX_VETOED);

    final HCNodeList ret = new HCNodeList ();
    ret.addChild (EText.MSG_EXECUTION_COUNT.getDisplayText (aDisplayLocale))
       .addChild (badgeSuccess (Long.toString (nExecutions)))
       .addChild (" ")
       .addChild (EText.MSG_ERROR_COUNT.getDisplayText (aDisplayLocale))
       .addChild (nErrors == 0 ? badgeSuccess (Long.toString (nErrors)) : badgeDanger (Long.toString (nErrors)));
    if (nVetoed > 0)
    {
      ret.addChild (" ")
         .addChild (EText.MSG_VETOED_COUNT.getDisplayText (aDisplayLocale))
         .addChild (badgeWarn (Long.toString (nVetoed)));
    }

    // The runtime of the finished executions, as collected since ph-schedule 6.2.0
    final String sTimerName = sStatsName + StatisticsJobListener.STATS_SUFFIX_TIME;
    // getTimerHandler creates the handler on demand, so the existence must be checked beforehand
    if (StatisticsManager.getAllTimerHandler ().contains (sTimerName))
    {
      final IStatisticsHandlerTimer aTimer = StatisticsManager.getTimerHandler (sTimerName);
      ret.addChild (div (EText.MSG_RUNTIME.getDisplayText (aDisplayLocale) +
                         EText.MSG_RUNTIME_VALUES.getDisplayTextWithArgs (aDisplayLocale,
                                                                          Long.toString (aTimer.getMin ()),
                                                                          Long.toString (aTimer.getAverage ()),
                                                                          Long.toString (aTimer.getMax ()))));
    }
    return ret;
  }

  /**
   * Create the UI representation of the most recent failures of a single job, as remembered by
   * {@link JobExecutionErrorRegistry} since ph-schedule 6.2.0. Only the stack trace of the most
   * recent failure is rendered - the older ones are listed with their message only, to keep the
   * page readable.
   *
   * @param aJobKey
   *        The job to get the errors for. May not be <code>null</code>.
   * @param aDisplayLocale
   *        The display locale to be used. May not be <code>null</code>.
   * @return <code>null</code> if the job never failed.
   */
  @Nullable
  private IHCNode _getJobErrorsUI (@NonNull final JobKey aJobKey, @NonNull final Locale aDisplayLocale)
  {
    final ICommonsList <JobExecutionError> aErrors = JobExecutionErrorRegistry.getAllErrorsOfJob (aJobKey);
    if (aErrors.isEmpty ())
      return null;

    // The registry delivers the oldest one first - show the most recent one on top
    aErrors.reverse ();

    final HCOL aOL = new HCOL ();
    boolean bFirst = true;
    for (final JobExecutionError aError : aErrors)
    {
      final HCLI aLI = aOL.addAndReturnItem (PDTToString.getAsString (aError.getErrorDateTime (), aDisplayLocale) +
                                             " - " +
                                             aError.getThrowableClassName () +
                                             (aError.getThrowableMessage () == null ? ""
                                                                                    : ": " +
                                                                                      aError.getThrowableMessage ()));
      if (bFirst)
      {
        // Only the most recent stack trace - deliberately shown as escaped source
        aLI.addChild (new HCPre ().addChild (aError.getStackTrace ()));
        bFirst = false;
      }
    }
    return new HCNodeList ().addChild (EText.MSG_LAST_ERRORS.getDisplayText (aDisplayLocale)).addChild (aOL);
  }

  /**
   * Create the UI representation of all jobs of a single scheduler that have no trigger at all and
   * are therefore never executed.
   *
   * @param aScheduler
   *        The scheduler to be evaluated. May not be <code>null</code>.
   * @param aDisplayLocale
   *        The display locale to be used. May not be <code>null</code>.
   * @return <code>null</code> if every registered job has at least one trigger.
   * @throws SchedulerException
   *         If querying the scheduler fails
   */
  @Nullable
  private IHCNode _getJobsWithoutTriggerUI (@NonNull final IScheduler aScheduler, @NonNull final Locale aDisplayLocale)
                                                                                                                        throws SchedulerException
  {
    final HCUL aUL = new HCUL ();
    for (final JobKey aJobKey : aScheduler.getJobKeys (GroupMatcher.anyJobGroup ()))
      if (aScheduler.getTriggersOfJob (aJobKey).isEmpty ())
        aUL.addItem (aJobKey.getAsString () + " - " + aScheduler.getJobDetail (aJobKey).getJobClass ().getName ());

    if (!aUL.hasChildren ())
      return null;

    return new HCNodeList ().addChild (warn (EText.MSG_JOBS_WITHOUT_TRIGGER_HINT.getDisplayText (aDisplayLocale)))
                            .addChild (aUL);
  }

  /**
   * Create the UI representation of all currently executing jobs of a single scheduler.
   *
   * @param aExecutingJobs
   *        The list of all currently executing jobs. May not be <code>null</code> but maybe empty.
   * @param aNow
   *        The current date time all values are compared to. May not be <code>null</code>.
   * @param aDisplayLocale
   *        The display locale to be used. May not be <code>null</code>.
   * @return The created node. Never <code>null</code>.
   */
  @NonNull
  private IHCNode _getExecutingJobsUI (@NonNull final List <IJobExecutionContext> aExecutingJobs,
                                       @NonNull final LocalDateTime aNow,
                                       @NonNull final Locale aDisplayLocale)
  {
    if (aExecutingJobs.isEmpty ())
      return em (EText.MSG_NONE.getDisplayText (aDisplayLocale));

    final HCOL aOL = new HCOL ();
    for (final IJobExecutionContext aJobCtx : aExecutingJobs)
    {
      final IJobDetail aDetail = aJobCtx.getJobDetail ();
      final HCLI aLI = aOL.addAndReturnItem (aDetail.getKey ().toString ());
      final HCUL aUL = aLI.addAndReturnChild (new HCUL ());
      aUL.addItem (EText.MSG_JOB_CLASS.getDisplayText (aDisplayLocale) + aDetail.getJobClass ().getName ());
      aUL.addItem (EText.MSG_TRIGGER_KEY.getDisplayText (aDisplayLocale) + aJobCtx.getTrigger ().getKey ().toString ());

      final LocalDateTime aFireTime = PDTFactory.createLocalDateTime (aJobCtx.getFireTime ());
      aUL.addItem (_getDateTimeUI (EText.MSG_FIRE_TIME.getDisplayText (aDisplayLocale),
                                   aFireTime,
                                   EDateTimeExpectation.PAST,
                                   aNow,
                                   aDisplayLocale));
      if (aFireTime != null)
        aUL.addItem (EText.MSG_RUNNING_FOR.getDisplayText (aDisplayLocale) +
                     Duration.between (aFireTime, aNow).toString ());
      aUL.addItem (_getDateTimeUI (EText.MSG_SCHEDULED_FIRE_TIME.getDisplayText (aDisplayLocale),
                                   PDTFactory.createLocalDateTime (aJobCtx.getScheduledFireTime ()),
                                   EDateTimeExpectation.PAST,
                                   aNow,
                                   aDisplayLocale));
      aUL.addItem (_getDateTimeUI (EText.MSG_NEXT_FIRE_TIME.getDisplayText (aDisplayLocale),
                                   PDTFactory.createLocalDateTime (aJobCtx.getNextFireTime ()),
                                   EDateTimeExpectation.FUTURE,
                                   aNow,
                                   aDisplayLocale));
      aUL.addItem (EText.MSG_REFIRE_COUNT.getDisplayText (aDisplayLocale) +
                   Integer.toString (aJobCtx.getRefireCount ()));
      aUL.addItem (EText.MSG_RECOVERING.getDisplayText (aDisplayLocale) +
                   EPhotonCoreText.getYesOrNo (aJobCtx.isRecovering (), aDisplayLocale));
      aUL.addItem (EText.MSG_FIRE_INSTANCE_ID.getDisplayText (aDisplayLocale) + aJobCtx.getFireInstanceId ());
    }
    return aOL;
  }

  @Override
  protected void fillContent (@NonNull final WPECTYPE aWPEC)
  {
    final HCNodeList aNodeList = aWPEC.getNodeList ();
    final Locale aDisplayLocale = aWPEC.getDisplayLocale ();
    // Compare all date times of this page against the same "now"
    final LocalDateTime aNow = PDTFactory.getCurrentLocalDateTime ();

    // Refresh button
    final BootstrapButtonToolbar aToolbar = new BootstrapButtonToolbar (aWPEC);
    aToolbar.addButton (EPhotonCoreText.BUTTON_REFRESH.getDisplayText (aDisplayLocale),
                        aWPEC.getSelfHref (),
                        EDefaultIcon.REFRESH);
    aNodeList.addChild (aToolbar);

    try
    {
      final BootstrapTabBox aTabBox = new BootstrapTabBox ();

      // For all schedulers
      for (final IScheduler aScheduler : QuartzSchedulerHelper.getSchedulerFactory ().getAllSchedulers ())
      {
        final HCNodeList aTab = new HCNodeList ();

        // Context
        final SchedulerContext aContext = aScheduler.getContext ();
        if (aContext.isNotEmpty ())
        {
          aTab.addChild (getUIHandler ().createActionHeader (EText.MSG_SCHEDULER_CONTEXT.getDisplayText (aDisplayLocale)));
          final HCTable aContextTable = new HCTable (HCCol.star (), HCCol.star ());
          for (final var aEntry : aContext.entrySet ())
            aContextTable.addBodyRow ().addCells (aEntry.getKey (), String.valueOf (aEntry.getValue ()));
          aTab.addChild (aContextTable);
        }

        final SchedulerMetaData aMetaData = aScheduler.getMetaData ();
        final ICommonsList <IJobExecutionContext> aExecutingJobs = aScheduler.getCurrentlyExecutingJobs ();

        // A scheduler that is not running executes nothing at all - that deserves more than a line
        // of prose in the summary
        final BootstrapBadge aStateBadge = switch (_getSchedulerState (aScheduler, aMetaData))
        {
          case SHUTDOWN ->
          {
            aTab.addChild (error (EText.MSG_SCHEDULER_SHUTDOWN_HINT.getDisplayText (aDisplayLocale)));
            yield badgeDanger (EText.MSG_SCHEDULER_STATE_SHUTDOWN.getDisplayText (aDisplayLocale));
          }
          case STANDBY ->
          {
            aTab.addChild (warn (EText.MSG_SCHEDULER_STANDBY_HINT.getDisplayText (aDisplayLocale)));
            yield badgeWarn (EText.MSG_SCHEDULER_STATE_STANDBY.getDisplayText (aDisplayLocale));
          }
          case NOT_STARTED ->
          {
            aTab.addChild (warn (EText.MSG_SCHEDULER_NOT_STARTED_HINT.getDisplayText (aDisplayLocale)));
            yield badgeWarn (EText.MSG_SCHEDULER_STATE_NOT_STARTED.getDisplayText (aDisplayLocale));
          }
          default -> badgeSuccess (EText.MSG_SCHEDULER_STATE_RUNNING.getDisplayText (aDisplayLocale));
        };
        // Jobs that have no trigger are never executed
        final IHCNode aJobsWithoutTrigger = _getJobsWithoutTriggerUI (aScheduler, aDisplayLocale);
        if (aJobsWithoutTrigger != null)
        {
          aTab.addChild (getUIHandler ().createActionHeader (EText.MSG_JOBS_WITHOUT_TRIGGER.getDisplayText (aDisplayLocale)));
          aTab.addChild (aJobsWithoutTrigger);
        }

        final BootstrapViewForm aDetailsForm = new BootstrapViewForm ();
        aDetailsForm.addFormGroup (new BootstrapFormGroup ().setLabel (EText.MSG_SCHEDULER_STATE.getDisplayText (aDisplayLocale))
                                                            .setCtrl (aStateBadge));
        aDetailsForm.addFormGroup (new BootstrapFormGroup ().setLabel (EText.MSG_RUNNING_SINCE.getDisplayText (aDisplayLocale))
                                                            .setCtrl (_getDateTimeUI ("",
                                                                                      PDTFactory.createLocalDateTime (aMetaData.getRunningSince ()),
                                                                                      EDateTimeExpectation.PAST,
                                                                                      aNow,
                                                                                      aDisplayLocale)));
        aDetailsForm.addFormGroup (new BootstrapFormGroup ().setLabel (EText.MSG_SUMMARY.getDisplayText (aDisplayLocale))
                                                            .setCtrl (HCExtHelper.nl2divList (aMetaData.getSummary ())));
        aDetailsForm.addFormGroup (new BootstrapFormGroup ().setLabel (EText.MSG_EXECUTING_JOBS.getDisplayText (aDisplayLocale))
                                                            .setCtrl (_getExecutingJobsUI (aExecutingJobs,
                                                                                           aNow,
                                                                                           aDisplayLocale)));

        // A saturated thread pool means that further jobs have to wait and may misfire
        final int nExecutingJobs = aExecutingJobs.size ();
        final int nThreadPoolSize = aMetaData.getThreadPoolSize ();
        final boolean bThreadPoolExhausted = nExecutingJobs >= nThreadPoolSize;
        final String sThreadPoolUsage = EText.MSG_THREAD_POOL_USAGE.getDisplayTextWithArgs (aDisplayLocale,
                                                                                            Integer.toString (nExecutingJobs),
                                                                                            Integer.toString (nThreadPoolSize));
        aDetailsForm.addFormGroup (new BootstrapFormGroup ().setLabel (EText.MSG_THREAD_POOL.getDisplayText (aDisplayLocale))
                                                            .setCtrl (bThreadPoolExhausted ? badgeDanger (sThreadPoolUsage)
                                                                                           : badgeSuccess (sThreadPoolUsage)));
        if (bThreadPoolExhausted)
          aTab.addChild (warn (EText.MSG_THREAD_POOL_EXHAUSTED.getDisplayText (aDisplayLocale)));

        // Triggers in a paused group do not fire at all
        final ICommonsSet <String> aPausedTriggerGroups = aScheduler.getPausedTriggerGroups ();
        aDetailsForm.addFormGroup (new BootstrapFormGroup ().setLabel (EText.MSG_PAUSED_TRIGGER_GROUPS.getDisplayText (aDisplayLocale))
                                                            .setCtrl (aPausedTriggerGroups.isEmpty () ? em (EText.MSG_NONE.getDisplayText (aDisplayLocale))
                                                                                                      : new HCNodeList ().addChild (warn (EText.MSG_PAUSED_TRIGGER_GROUPS_HINT.getDisplayText (aDisplayLocale)))
                                                                                                                         .addChildren (HCExtHelper.list2divList (aPausedTriggerGroups))));

        // Exclusion calendars silently suppress fire times
        final ICommonsList <String> aCalendarNames = aScheduler.getCalendarNames ();
        aDetailsForm.addFormGroup (new BootstrapFormGroup ().setLabel (EText.MSG_CALENDARS.getDisplayText (aDisplayLocale))
                                                            .setCtrl (aCalendarNames.isEmpty () ? em (EText.MSG_NONE.getDisplayText (aDisplayLocale))
                                                                                                : new HCNodeList ().addChildren (HCExtHelper.list2divList (aCalendarNames))));

        // All job listener
        final ICommonsList <String> aListeners = new CommonsArrayList <> ();
        for (final IJobListener aJobListener : aScheduler.getListenerManager ().getJobListeners ())
          aListeners.add (aJobListener.getName () + " - " + aJobListener.getClass ().getName ());
        aDetailsForm.addFormGroup (new BootstrapFormGroup ().setLabel (EText.MSG_LISTENERS.getDisplayText (aDisplayLocale))
                                                            .setCtrl (HCExtHelper.list2divList (aListeners)));
        aTab.addChild (aDetailsForm);

        // Add all scheduled jobs
        final HCOL aDetailOL = new HCOL ();
        for (final String sTriggerGroupName : aScheduler.getTriggerGroupNames ())
          for (final TriggerKey aTriggerKey : aScheduler.getTriggerKeys (GroupMatcher.triggerGroupEquals (sTriggerGroupName)))
          {
            final ITrigger aTrigger = aScheduler.getTrigger (aTriggerKey);
            final JobKey aJobKey = aTrigger.getJobKey ();
            final IJobDetail aDetail = aScheduler.getJobDetail (aJobKey);
            final HCLI aLI = aDetailOL.addAndReturnItem (aJobKey.getName ());
            final HCUL aUL2 = aLI.addAndReturnChild (new HCUL ());
            aUL2.addItem (EText.MSG_JOB_CLASS.getDisplayText (aDisplayLocale) + aDetail.getJobClass ().getName ());
            aUL2.addItem (EText.MSG_TRIGGER_CLASS.getDisplayText (aDisplayLocale) + aTrigger.getClass ().getName ());
            aUL2.addItem (EText.MSG_TRIGGER_KEY.getDisplayText (aDisplayLocale) + aTrigger.getKey ().toString ());
            aUL2.addItem (new HCTextNode (EText.MSG_TRIGGER_STATE.getDisplayText (aDisplayLocale)),
                          _getTriggerStateBadge (aScheduler.getTriggerState (aTriggerKey)));
            if (StringHelper.isNotEmpty (aTrigger.getCalendarName ()))
              aUL2.addItem (EText.MSG_CALENDAR_NAME.getDisplayText (aDisplayLocale) + aTrigger.getCalendarName ());
            aUL2.addItem (_getDateTimeUI (EText.MSG_START_TIME.getDisplayText (aDisplayLocale),
                                          PDTFactory.createLocalDateTime (aTrigger.getStartTime ()),
                                          EDateTimeExpectation.PAST,
                                          aNow,
                                          aDisplayLocale));
            if (aTrigger.getEndTime () != null)
              aUL2.addItem (_getDateTimeUI (EText.MSG_END_TIME.getDisplayText (aDisplayLocale),
                                            PDTFactory.createLocalDateTime (aTrigger.getEndTime ()),
                                            EDateTimeExpectation.FUTURE,
                                            aNow,
                                            aDisplayLocale));
            aUL2.addItem (_getDateTimeUI (EText.MSG_PREVIOUS_FIRE_TIME.getDisplayText (aDisplayLocale),
                                          PDTFactory.createLocalDateTime (aTrigger.getPreviousFireTime ()),
                                          EDateTimeExpectation.PAST,
                                          aNow,
                                          aDisplayLocale));
            aUL2.addItem (_getDateTimeUI (EText.MSG_NEXT_FIRE_TIME.getDisplayText (aDisplayLocale),
                                          PDTFactory.createLocalDateTime (aTrigger.getNextFireTime ()),
                                          EDateTimeExpectation.FUTURE,
                                          aNow,
                                          aDisplayLocale));

            // Tells apart "paused or broken" from "simply exhausted" if there is no next fire time
            final boolean bMayFireAgain = aTrigger.mayFireAgain ();
            aUL2.addItem (new HCTextNode (EText.MSG_MAY_FIRE_AGAIN.getDisplayText (aDisplayLocale)),
                          bMayFireAgain ? badgeSuccess (EPhotonCoreText.getYesOrNo (true, aDisplayLocale))
                                        : badgeWarn (EPhotonCoreText.getYesOrNo (false, aDisplayLocale)));
            // Is null if the trigger repeats indefinitely, and may legitimately be in the past
            aUL2.addItem (_getDateTimeUI (EText.MSG_FINAL_FIRE_TIME.getDisplayText (aDisplayLocale),
                                          PDTFactory.createLocalDateTime (aTrigger.getFinalFireTime ()),
                                          EDateTimeExpectation.ANY,
                                          aNow,
                                          aDisplayLocale));
            final IHCNode aNextFireTimesUI = _getNextFireTimesUI (aTrigger, aDisplayLocale);
            if (aNextFireTimesUI != null)
              aUL2.addItem (aNextFireTimesUI);
            final IHCNode aJobStatisticsUI = _getJobStatisticsUI (aDetail.getJobClass (), aDisplayLocale);
            if (aJobStatisticsUI != null)
              aUL2.addItem (aJobStatisticsUI);
            final IHCNode aJobErrorsUI = _getJobErrorsUI (aJobKey, aDisplayLocale);
            if (aJobErrorsUI != null)
              aUL2.addItem (aJobErrorsUI);

            if (aTrigger.getMisfireInstruction () != null)
              aUL2.addItem (EText.MSG_MISFIRE_INSTRUCTIONS.getDisplayText (aDisplayLocale) +
                            StringHelper.trimStart (aTrigger.getMisfireInstruction ().name (), "MISFIRE_INSTRUCTION_"));

            final BootstrapTable aJobDataTable = new BootstrapTable (HCCol.star (), HCCol.star ());
            aJobDataTable.setCondensed (true);
            for (final Map.Entry <String, Object> aEntry : aDetail.getJobDataMap ().entrySet ())
              aJobDataTable.addBodyRow ().addCells (aEntry.getKey (), String.valueOf (aEntry.getValue ()));

            aUL2.addItem (new HCTextNode (EText.MSG_JOB_DATA.getDisplayText (aDisplayLocale)),
                          aJobDataTable.hasBodyRows () ? aJobDataTable
                                                       : em (EText.MSG_NONE.getDisplayText (aDisplayLocale)));
          }
        aTab.addChild (aDetailOL);

        aTabBox.addTab (aScheduler.getSchedulerName (), aScheduler.getSchedulerName (), aTab);
      }

      if (aTabBox.hasNoTabs ())
        aNodeList.addChild (info (EText.MSG_NOTHING_SCHEDULED.getDisplayText (aDisplayLocale)));
      else
        aNodeList.addChild (aTabBox);
    }
    catch (final SchedulerException ex)
    {
      aNodeList.addChild (error (ex.getMessage ()));
    }
  }
}
