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
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import com.helger.annotation.Nonempty;
import com.helger.annotation.misc.Translatable;
import com.helger.base.string.StringHelper;
import com.helger.collection.commons.CommonsArrayList;
import com.helger.collection.commons.ICommonsList;
import com.helger.datetime.format.PDTToString;
import com.helger.datetime.helper.PDTFactory;
import com.helger.datetime.util.PDTDisplayHelper;
import com.helger.datetime.util.PDTDisplayHelper.IPeriodTextProvider;
import com.helger.html.hc.IHCNode;
import com.helger.html.hc.ext.HCExtHelper;
import com.helger.html.hc.html.grouping.HCLI;
import com.helger.html.hc.html.grouping.HCOL;
import com.helger.html.hc.html.grouping.HCUL;
import com.helger.html.hc.html.tabular.HCCol;
import com.helger.html.hc.html.tabular.HCTable;
import com.helger.html.hc.impl.HCNodeList;
import com.helger.html.hc.impl.HCTextNode;
import com.helger.photon.bootstrap5.badge.BootstrapBadge;
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
import com.helger.quartz.IJobDetail;
import com.helger.quartz.IJobExecutionContext;
import com.helger.quartz.IJobListener;
import com.helger.quartz.IScheduler;
import com.helger.quartz.ITrigger;
import com.helger.quartz.JobKey;
import com.helger.quartz.SchedulerContext;
import com.helger.quartz.SchedulerException;
import com.helger.quartz.TriggerKey;
import com.helger.quartz.impl.matchers.GroupMatcher;
import com.helger.schedule.quartz.QuartzSchedulerHelper;
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
   * @param bExpectedInFuture
   *        <code>true</code> if the date time is expected to be &ge; now - as e.g. the next fire
   *        time. <code>false</code> if it is expected to be &le; now - as e.g. the previous fire
   *        time.
   * @param aNow
   *        The current date time all values are compared to. May not be <code>null</code>.
   * @param aDisplayLocale
   *        The display locale to be used. May not be <code>null</code>.
   * @return The created badge. Never <code>null</code>.
   */
  @NonNull
  private BootstrapBadge _getRelativeTimeBadge (@NonNull final LocalDateTime aDateTime,
                                                final boolean bExpectedInFuture,
                                                @NonNull final LocalDateTime aNow,
                                                @NonNull final Locale aDisplayLocale)
  {
    final boolean bIsInFuture = aDateTime.isAfter (aNow);
    // "now" itself fulfills both ">= now" and "<= now"
    final boolean bIsAsExpected = bExpectedInFuture ? !aDateTime.isBefore (aNow) : !aDateTime.isAfter (aNow);

    final IPeriodTextProvider aTextProvider = "de".equals (aDisplayLocale.getLanguage ()) ? IPeriodTextProvider.DE
                                                                                          : IPeriodTextProvider.EN;
    final String sPeriod = bIsInFuture ? PDTDisplayHelper.getPeriodText (aNow, aDateTime, aTextProvider)
                                       : PDTDisplayHelper.getPeriodText (aDateTime, aNow, aTextProvider);
    final String sText = bIsInFuture ? EText.MSG_TIME_IN.getDisplayTextWithArgs (aDisplayLocale, sPeriod)
                                     : EText.MSG_TIME_AGO.getDisplayTextWithArgs (aDisplayLocale, sPeriod);
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
   * @param bExpectedInFuture
   *        <code>true</code> if the date time is expected to be &ge; now, <code>false</code> if it
   *        is expected to be &le; now.
   * @param aNow
   *        The current date time all values are compared to. May not be <code>null</code>.
   * @param aDisplayLocale
   *        The display locale to be used. May not be <code>null</code>.
   * @return The created node. Never <code>null</code>.
   */
  @NonNull
  private IHCNode _getDateTimeUI (@NonNull final String sLabel,
                                  @Nullable final LocalDateTime aDateTime,
                                  final boolean bExpectedInFuture,
                                  @NonNull final LocalDateTime aNow,
                                  @NonNull final Locale aDisplayLocale)
  {
    final HCNodeList ret = new HCNodeList ().addChild (sLabel);
    if (aDateTime == null)
      ret.addChild (em (EText.MSG_NONE.getDisplayText (aDisplayLocale)));
    else
      ret.addChild (PDTToString.getAsString (aDateTime, aDisplayLocale))
         .addChild (" ")
         .addChild (_getRelativeTimeBadge (aDateTime, bExpectedInFuture, aNow, aDisplayLocale));
    return ret;
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
                                   false,
                                   aNow,
                                   aDisplayLocale));
      if (aFireTime != null)
        aUL.addItem (EText.MSG_RUNNING_FOR.getDisplayText (aDisplayLocale) +
                     Duration.between (aFireTime, aNow).toString ());
      aUL.addItem (_getDateTimeUI (EText.MSG_SCHEDULED_FIRE_TIME.getDisplayText (aDisplayLocale),
                                   PDTFactory.createLocalDateTime (aJobCtx.getScheduledFireTime ()),
                                   false,
                                   aNow,
                                   aDisplayLocale));
      aUL.addItem (_getDateTimeUI (EText.MSG_NEXT_FIRE_TIME.getDisplayText (aDisplayLocale),
                                   PDTFactory.createLocalDateTime (aJobCtx.getNextFireTime ()),
                                   true,
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

        final BootstrapViewForm aDetailsForm = new BootstrapViewForm ();
        aDetailsForm.addFormGroup (new BootstrapFormGroup ().setLabel (EText.MSG_SUMMARY.getDisplayText (aDisplayLocale))
                                                            .setCtrl (HCExtHelper.nl2divList (aScheduler.getMetaData ()
                                                                                                        .getSummary ())));
        aDetailsForm.addFormGroup (new BootstrapFormGroup ().setLabel (EText.MSG_EXECUTING_JOBS.getDisplayText (aDisplayLocale))
                                                            .setCtrl (_getExecutingJobsUI (aScheduler.getCurrentlyExecutingJobs (),
                                                                                           aNow,
                                                                                           aDisplayLocale)));

        // All job listener
        final ICommonsList <String> aListeners = new CommonsArrayList <> ();
        for (final IJobListener aJobListener : aScheduler.getListenerManager ().getJobListeners ())
          aListeners.add (aJobListener.getName () + " - " + aJobListener.getClass ().getName ());
        aDetailsForm.addFormGroup (new BootstrapFormGroup ().setLabel (EText.MSG_LISTENERS.getDisplayText (aDisplayLocale))
                                                            .setCtrl (HCExtHelper.list2divList (aListeners)));
        aTab.addChild (aDetailsForm);

        // Add all scheduled jobs
        final HCUL aDetailUL = new HCUL ();
        for (final String sTriggerGroupName : aScheduler.getTriggerGroupNames ())
          for (final TriggerKey aTriggerKey : aScheduler.getTriggerKeys (GroupMatcher.triggerGroupEquals (sTriggerGroupName)))
          {
            final ITrigger aTrigger = aScheduler.getTrigger (aTriggerKey);
            final JobKey aJobKey = aTrigger.getJobKey ();
            final IJobDetail aDetail = aScheduler.getJobDetail (aJobKey);
            final HCLI aLI = aDetailUL.addAndReturnItem (aJobKey.getName ());
            final HCUL aUL2 = aLI.addAndReturnChild (new HCUL ());
            aUL2.addItem (EText.MSG_JOB_CLASS.getDisplayText (aDisplayLocale) + aDetail.getJobClass ().getName ());
            aUL2.addItem (EText.MSG_TRIGGER_CLASS.getDisplayText (aDisplayLocale) + aTrigger.getClass ().getName ());
            aUL2.addItem (EText.MSG_TRIGGER_KEY.getDisplayText (aDisplayLocale) + aTrigger.getKey ().toString ());
            aUL2.addItem (_getDateTimeUI (EText.MSG_START_TIME.getDisplayText (aDisplayLocale),
                                          PDTFactory.createLocalDateTime (aTrigger.getStartTime ()),
                                          false,
                                          aNow,
                                          aDisplayLocale));
            if (aTrigger.getEndTime () != null)
              aUL2.addItem (_getDateTimeUI (EText.MSG_END_TIME.getDisplayText (aDisplayLocale),
                                            PDTFactory.createLocalDateTime (aTrigger.getEndTime ()),
                                            true,
                                            aNow,
                                            aDisplayLocale));
            aUL2.addItem (_getDateTimeUI (EText.MSG_PREVIOUS_FIRE_TIME.getDisplayText (aDisplayLocale),
                                          PDTFactory.createLocalDateTime (aTrigger.getPreviousFireTime ()),
                                          false,
                                          aNow,
                                          aDisplayLocale));
            aUL2.addItem (_getDateTimeUI (EText.MSG_NEXT_FIRE_TIME.getDisplayText (aDisplayLocale),
                                          PDTFactory.createLocalDateTime (aTrigger.getNextFireTime ()),
                                          true,
                                          aNow,
                                          aDisplayLocale));
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
        aTab.addChild (aDetailUL);

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
