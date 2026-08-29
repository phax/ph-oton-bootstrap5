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

import java.io.File;
import java.time.Duration;
import java.util.Locale;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.annotation.Nonempty;
import com.helger.annotation.misc.Translatable;
import com.helger.annotation.style.OverrideOnDemand;
import com.helger.base.compare.ESortOrder;
import com.helger.base.enforce.ValueEnforcer;
import com.helger.collection.commons.ICommonsList;
import com.helger.datetime.format.PDTToString;
import com.helger.html.hc.IHCNode;
import com.helger.html.hc.ext.HCExtHelper;
import com.helger.html.hc.html.grouping.HCPre;
import com.helger.html.hc.html.tabular.HCRow;
import com.helger.html.hc.html.tabular.HCTable;
import com.helger.html.hc.html.textlevel.HCA;
import com.helger.html.hc.impl.HCNodeList;
import com.helger.io.misc.SizeHelper;
import com.helger.photon.bootstrap5.button.BootstrapButton;
import com.helger.photon.bootstrap5.buttongroup.BootstrapButtonToolbar;
import com.helger.photon.bootstrap5.form.BootstrapForm;
import com.helger.photon.bootstrap5.form.BootstrapFormGroup;
import com.helger.photon.bootstrap5.form.BootstrapViewForm;
import com.helger.photon.bootstrap5.pages.AbstractBootstrapWebPageForm;
import com.helger.photon.bootstrap5.pages.handler.AbstractBootstrapWebPageActionHandler;
import com.helger.photon.bootstrap5.pages.handler.AbstractBootstrapWebPageActionHandlerDelete;
import com.helger.photon.bootstrap5.uictrls.datatables.BootstrapDataTables;
import com.helger.photon.core.EPhotonCoreText;
import com.helger.photon.core.form.FormErrorList;
import com.helger.photon.mgrs.longrun.ILongRunningJobResultManager;
import com.helger.photon.mgrs.longrun.LongRunningJobData;
import com.helger.photon.mgrs.longrun.LongRunningJobManager;
import com.helger.photon.mgrs.longrun.LongRunningJobResult;
import com.helger.photon.security.util.SecurityHelper;
import com.helger.photon.uicore.css.CPageParam;
import com.helger.photon.uicore.icon.EDefaultIcon;
import com.helger.photon.uicore.page.EShowList;
import com.helger.photon.uicore.page.EWebPageFormAction;
import com.helger.photon.uicore.page.EWebPageText;
import com.helger.photon.uicore.page.IWebPageExecutionContext;
import com.helger.photon.uictrls.datatables.DataTables;
import com.helger.photon.uictrls.datatables.column.DTCol;
import com.helger.photon.uictrls.datatables.column.EDTColType;
import com.helger.text.IMultilingualText;
import com.helger.text.display.IHasDisplayTextWithArgs;
import com.helger.text.resolve.DefaultTextResolver;
import com.helger.text.util.TextHelper;
import com.helger.url.ISimpleURL;

/**
 * Show all currently running long running jobs as well as the results of all previously finished
 * long running jobs.
 *
 * @author Philip Helger
 * @param <WPECTYPE>
 *        Web page execution context type
 * @since 0.9.3
 */
public class BasePageAppInfoLongRunningJobs <WPECTYPE extends IWebPageExecutionContext> extends
                                            AbstractBootstrapWebPageForm <LongRunningJobData, WPECTYPE>
{
  @Translatable
  protected enum EText implements IHasDisplayTextWithArgs
  {
    MSG_ID ("ID", "ID"),
    MSG_JOB_TYPE ("Job-Typ", "Job type"),
    MSG_DESCRIPTION ("Beschreibung", "Description"),
    MSG_START_DT ("Startzeit", "Start time"),
    MSG_END_DT ("Endzeit", "End time"),
    MSG_DURATION ("Dauer", "Duration"),
    MSG_USER ("Benutzer", "User"),
    MSG_SUCCESS ("Erfolg?", "Success?"),
    MSG_STATE ("Status", "State"),
    MSG_STATE_RUNNING ("Läuft", "Running"),
    MSG_STATE_FINISHED ("Beendet", "Finished"),
    MSG_RESULT ("Ergebnis", "Result"),
    MSG_RESULT_TYPE ("Ergebnistyp", "Result type"),
    MSG_FILE_MISSING ("Die Datei existiert nicht (mehr).", "The file does not (or no longer) exist."),
    MSG_FILE_SIZE ("Dateigröße: {0}", "File size: {0}"),
    DELETE_QUERY ("Soll das Ergebnis des Jobs ''{0}'' wirklich gelöscht werden?",
                  "Should the result of job ''{0}'' really be deleted?"),
    DELETE_HINT ("Hinweis: eine allfällige Ergebnisdatei auf der Festplatte wird dabei nicht gelöscht.",
                 "Note: a possibly existing result file on disk is not deleted by this action."),
    DELETE_SUCCESS ("Das Job-Ergebnis wurde erfolgreich gelöscht.", "The job result was successfully deleted."),
    DELETE_ALL_SUCCESS_1 ("Es wurde 1 Job-Ergebnis erfolgreich gelöscht.", "1 job result was successfully deleted."),
    DELETE_ALL_SUCCESS_N ("Es wurden {0} Job-Ergebnisse erfolgreich gelöscht.",
                          "{0} job results were successfully deleted.");

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

  private static final Logger LOGGER = LoggerFactory.getLogger (BasePageAppInfoLongRunningJobs.class);

  private final LongRunningJobManager m_aJobMgr;
  private final ILongRunningJobResultManager m_aResultMgr;

  private void _init ()
  {
    setDeleteHandler (new AbstractBootstrapWebPageActionHandlerDelete <LongRunningJobData, WPECTYPE> ()
    {
      @Override
      protected void showQuery (@NonNull final WPECTYPE aWPEC,
                                @NonNull final BootstrapForm aForm,
                                @Nullable final LongRunningJobData aSelectedObject)
      {
        assert aSelectedObject != null;
        final Locale aDisplayLocale = aWPEC.getDisplayLocale ();

        aForm.addChild (question (EText.DELETE_QUERY.getDisplayTextWithArgs (aDisplayLocale,
                                                                             aSelectedObject.getJobDescription ()
                                                                                            .getText (aDisplayLocale))));
        aForm.addChild (info (EText.DELETE_HINT.getDisplayText (aDisplayLocale)));
      }

      @Override
      protected void performAction (@NonNull final WPECTYPE aWPEC, @Nullable final LongRunningJobData aSelectedObject)
      {
        assert aSelectedObject != null;
        final Locale aDisplayLocale = aWPEC.getDisplayLocale ();

        if (m_aResultMgr.deleteResult (aSelectedObject.getID ()).isChanged ())
        {
          LOGGER.info ("Deleted the long running job result with ID '" + aSelectedObject.getID () + "'");
          aWPEC.postRedirectGetInternal (success (EText.DELETE_SUCCESS.getDisplayText (aDisplayLocale)));
        }
      }
    });
    addCustomHandler (CPageParam.ACTION_DELETE_ALL,
                      new AbstractBootstrapWebPageActionHandler <LongRunningJobData, WPECTYPE> (false)
                      {
                        @NonNull
                        public EShowList handleAction (@NonNull final WPECTYPE aWPEC,
                                                       @Nullable final LongRunningJobData aSelectedObject)
                        {
                          final Locale aDisplayLocale = aWPEC.getDisplayLocale ();

                          int nDeleted = 0;
                          for (final LongRunningJobData aJobData : m_aResultMgr.getAllJobResults ())
                            if (m_aResultMgr.deleteResult (aJobData.getID ()).isChanged ())
                              nDeleted++;

                          if (nDeleted > 0)
                          {
                            LOGGER.info ("Deleted " + nDeleted + " long running job results");
                            final String sSuccessMsg = nDeleted == 1 ? EText.DELETE_ALL_SUCCESS_1.getDisplayText (aDisplayLocale)
                                                                     : EText.DELETE_ALL_SUCCESS_N.getDisplayTextWithArgs (aDisplayLocale,
                                                                                                                          Integer.toString (nDeleted));
                            aWPEC.postRedirectGetInternal (success (sSuccessMsg));
                          }
                          return EShowList.SHOW_LIST;
                        }
                      });
  }

  public BasePageAppInfoLongRunningJobs (@NonNull @Nonempty final String sID,
                                         @NonNull final LongRunningJobManager aJobMgr,
                                         @NonNull final ILongRunningJobResultManager aResultMgr)
  {
    this (sID, EWebPageText.PAGE_NAME_APPINFO_LONG_RUNNING_JOBS.getAsMLT (), null, aJobMgr, aResultMgr);
  }

  public BasePageAppInfoLongRunningJobs (@NonNull @Nonempty final String sID,
                                         @NonNull final IMultilingualText aName,
                                         @Nullable final IMultilingualText aDescription,
                                         @NonNull final LongRunningJobManager aJobMgr,
                                         @NonNull final ILongRunningJobResultManager aResultMgr)
  {
    super (sID, aName, aDescription);
    m_aJobMgr = ValueEnforcer.notNull (aJobMgr, "JobMgr");
    m_aResultMgr = ValueEnforcer.notNull (aResultMgr, "ResultMgr");
    _init ();
  }

  @NonNull
  protected final LongRunningJobManager getJobMgr ()
  {
    return m_aJobMgr;
  }

  @NonNull
  protected final ILongRunningJobResultManager getResultMgr ()
  {
    return m_aResultMgr;
  }

  @Override
  protected boolean isActionAllowed (@NonNull final WPECTYPE aWPEC,
                                     @NonNull final EWebPageFormAction eFormAction,
                                     @Nullable final LongRunningJobData aSelectedObject)
  {
    if (eFormAction.isEdit ())
      return false;
    // A running job has no result yet, so there is nothing to be deleted
    if (eFormAction.isDelete () && aSelectedObject != null && !aSelectedObject.isEnded ())
      return false;
    return super.isActionAllowed (aWPEC, eFormAction, aSelectedObject);
  }

  @Override
  @Nullable
  protected LongRunningJobData getSelectedObject (@NonNull final WPECTYPE aWPEC, @Nullable final String sID)
  {
    // First the finished ones, because they are the majority
    final LongRunningJobData aResult = m_aResultMgr.getJobResultOfID (sID);
    if (aResult != null)
      return aResult;

    // Afterwards the currently running ones
    if (sID != null)
      for (final LongRunningJobData aJobData : m_aJobMgr.getAllRunningJobs ())
        if (sID.equals (aJobData.getID ()))
          return aJobData;
    return null;
  }

  @Override
  protected void modifyViewToolbar (@NonNull final WPECTYPE aWPEC,
                                    @NonNull final LongRunningJobData aSelectedObject,
                                    @NonNull final BootstrapButtonToolbar aToolbar)
  {
    if (aSelectedObject.isEnded ())
    {
      final Locale aDisplayLocale = aWPEC.getDisplayLocale ();
      aToolbar.addButton (EPhotonCoreText.BUTTON_DELETE.getDisplayText (aDisplayLocale),
                          aWPEC.getSelfHref ()
                               .add (CPageParam.PARAM_ACTION, CPageParam.ACTION_DELETE)
                               .add (CPageParam.PARAM_OBJECT, aSelectedObject.getID ()),
                          EDefaultIcon.DELETE);
    }
  }

  /**
   * Create the UI representation of a single job result.
   *
   * @param aResult
   *        The result to be displayed. May not be <code>null</code>.
   * @param aDisplayLocale
   *        The display locale to be used. May not be <code>null</code>.
   * @return The created node. Never <code>null</code>.
   */
  @NonNull
  @OverrideOnDemand
  protected IHCNode getResultUI (@NonNull final LongRunningJobResult aResult, @NonNull final Locale aDisplayLocale)
  {
    switch (aResult.getType ())
    {
      case TEXT:
        return new HCNodeList ().addChildren (HCExtHelper.nl2divList (aResult.getResultText ()));
      case LINK:
      {
        final ISimpleURL aURL = aResult.getResultLink ();
        return new HCA (aURL).addChild (aURL.getAsString ());
      }
      case FILE:
      {
        final File aFile = aResult.getResultFile ();
        final HCNodeList ret = new HCNodeList ().addChild (div (aFile.getAbsolutePath ()));
        if (aFile.isFile ())
        {
          final SizeHelper aSH = SizeHelper.getSizeHelperOfLocale (aDisplayLocale);
          ret.addChild (div (EText.MSG_FILE_SIZE.getDisplayTextWithArgs (aDisplayLocale,
                                                                         aSH.getAsMatching (aFile.length (), 2))));
        }
        else
          ret.addChild (warn (EText.MSG_FILE_MISSING.getDisplayText (aDisplayLocale)));
        return ret;
      }
      default:
        // XML and JSON - deliberately shown as escaped source and not rendered
        return new HCPre ().addChild (aResult.getAsString ());
    }
  }

  @Override
  protected void showSelectedObject (@NonNull final WPECTYPE aWPEC, @NonNull final LongRunningJobData aSelectedObject)
  {
    final HCNodeList aNodeList = aWPEC.getNodeList ();
    final Locale aDisplayLocale = aWPEC.getDisplayLocale ();

    final BootstrapViewForm aForm = aNodeList.addAndReturnChild (new BootstrapViewForm ());
    aForm.addFormGroup (new BootstrapFormGroup ().setLabel (EText.MSG_JOB_TYPE.getDisplayText (aDisplayLocale))
                                                 .setCtrl (aSelectedObject.getJobType ()));
    aForm.addFormGroup (new BootstrapFormGroup ().setLabel (EText.MSG_ID.getDisplayText (aDisplayLocale))
                                                 .setCtrl (aSelectedObject.getID ()));
    aForm.addFormGroup (new BootstrapFormGroup ().setLabel (EText.MSG_DESCRIPTION.getDisplayText (aDisplayLocale))
                                                 .setCtrl (aSelectedObject.getJobDescription ()
                                                                          .getText (aDisplayLocale)));
    aForm.addFormGroup (new BootstrapFormGroup ().setLabel (EText.MSG_START_DT.getDisplayText (aDisplayLocale))
                                                 .setCtrl (PDTToString.getAsString (aSelectedObject.getStartDateTime (),
                                                                                    aDisplayLocale)));
    aForm.addFormGroup (new BootstrapFormGroup ().setLabel (EText.MSG_USER.getDisplayText (aDisplayLocale))
                                                 .setCtrl (SecurityHelper.getUserDisplayName (aSelectedObject.getStartingUserID (),
                                                                                              aDisplayLocale)));
    if (aSelectedObject.isEnded ())
    {
      aForm.addFormGroup (new BootstrapFormGroup ().setLabel (EText.MSG_END_DT.getDisplayText (aDisplayLocale))
                                                   .setCtrl (PDTToString.getAsString (aSelectedObject.getEndDateTime (),
                                                                                      aDisplayLocale)));
      aForm.addFormGroup (new BootstrapFormGroup ().setLabel (EText.MSG_DURATION.getDisplayText (aDisplayLocale))
                                                   .setCtrl (aSelectedObject.getDuration ().toString ()));
      aForm.addFormGroup (new BootstrapFormGroup ().setLabel (EText.MSG_SUCCESS.getDisplayText (aDisplayLocale))
                                                   .setCtrl (EPhotonCoreText.getYesOrNo (aSelectedObject.getExecutionSuccess ()
                                                                                                        .isTrue (),
                                                                                         aDisplayLocale)));
      final LongRunningJobResult aResult = aSelectedObject.getResult ();
      if (aResult != null)
      {
        aForm.addFormGroup (new BootstrapFormGroup ().setLabel (EText.MSG_RESULT_TYPE.getDisplayText (aDisplayLocale))
                                                     .setCtrl (aResult.getType ().getID ()));
        aForm.addFormGroup (new BootstrapFormGroup ().setLabel (EText.MSG_RESULT.getDisplayText (aDisplayLocale))
                                                     .setCtrl (getResultUI (aResult, aDisplayLocale)));
      }
    }
    else
    {
      aForm.addFormGroup (new BootstrapFormGroup ().setLabel (EText.MSG_STATE.getDisplayText (aDisplayLocale))
                                                   .setCtrl (EText.MSG_STATE_RUNNING.getDisplayText (aDisplayLocale)));
    }
  }

  @Override
  protected void validateAndSaveInputParameters (@NonNull final WPECTYPE aWPEC,
                                                 @Nullable final LongRunningJobData aSelectedObject,
                                                 @NonNull final FormErrorList aFormErrors,
                                                 @NonNull final EWebPageFormAction eFormAction)
  {
    throw new UnsupportedOperationException ();
  }

  @Override
  protected void showInputForm (@NonNull final WPECTYPE aWPEC,
                                @Nullable final LongRunningJobData aSelectedObject,
                                @NonNull final BootstrapForm aForm,
                                final boolean bIsFormSubmitted,
                                @NonNull final EWebPageFormAction eFormAction,
                                @NonNull final FormErrorList aFormErrors)
  {
    throw new UnsupportedOperationException ();
  }

  private void _addRow (@NonNull final WPECTYPE aWPEC,
                        @NonNull final HCTable aTable,
                        @NonNull final LongRunningJobData aItem,
                        final boolean bIsRunning)
  {
    final Locale aDisplayLocale = aWPEC.getDisplayLocale ();
    final ISimpleURL aViewURL = createViewURL (aWPEC, aItem);
    final Duration aDuration = aItem.isEnded () ? aItem.getDuration () : null;

    final HCRow aRow = aTable.addBodyRow ();
    aRow.addCell (new HCA (aViewURL).addChild (aItem.getJobDescription ().getText (aDisplayLocale)));
    aRow.addCell (aItem.getJobType ());
    aRow.addCell (bIsRunning ? EText.MSG_STATE_RUNNING.getDisplayText (aDisplayLocale)
                             : EText.MSG_STATE_FINISHED.getDisplayText (aDisplayLocale));
    aRow.addCell (PDTToString.getAsString (aItem.getStartDateTime (), aDisplayLocale));
    aRow.addCell (aItem.isEnded () ? PDTToString.getAsString (aItem.getEndDateTime (), aDisplayLocale) : null);
    aRow.addCell (aDuration == null ? null : aDuration.toString ());
    aRow.addCell (SecurityHelper.getUserDisplayName (aItem.getStartingUserID (), aDisplayLocale));
    aRow.addCell (aItem.isEnded () ? EPhotonCoreText.getYesOrNo (aItem.getExecutionSuccess ().isTrue (), aDisplayLocale)
                                   : null);
  }

  @Override
  protected void showListOfExistingObjects (@NonNull final WPECTYPE aWPEC)
  {
    final HCNodeList aNodeList = aWPEC.getNodeList ();
    final Locale aDisplayLocale = aWPEC.getDisplayLocale ();

    final ICommonsList <LongRunningJobData> aAllResults = m_aResultMgr.getAllJobResults ();

    final BootstrapButtonToolbar aToolbar = new BootstrapButtonToolbar (aWPEC);
    aToolbar.addButton (EPhotonCoreText.BUTTON_REFRESH.getDisplayText (aDisplayLocale),
                        aWPEC.getSelfHref (),
                        EDefaultIcon.REFRESH);
    aToolbar.addChild (new BootstrapButton ().addChild (EPhotonCoreText.BUTTON_DELETE_ALL.getDisplayText (aDisplayLocale))
                                             .setOnClick (aWPEC.getSelfHref ()
                                                               .add (CPageParam.PARAM_ACTION,
                                                                     CPageParam.ACTION_DELETE_ALL))
                                             .setIcon (EDefaultIcon.DELETE)
                                             .setDisabled (aAllResults.isEmpty ()));
    aNodeList.addChild (aToolbar);

    final HCTable aTable = new HCTable (new DTCol (EText.MSG_DESCRIPTION.getDisplayText (aDisplayLocale)),
                                        new DTCol (EText.MSG_JOB_TYPE.getDisplayText (aDisplayLocale)),
                                        new DTCol (EText.MSG_STATE.getDisplayText (aDisplayLocale)),
                                        new DTCol (EText.MSG_START_DT.getDisplayText (aDisplayLocale)).setDisplayType (EDTColType.DATETIME,
                                                                                                                       aDisplayLocale)
                                                                                                      .setInitialSorting (ESortOrder.DESCENDING),
                                        new DTCol (EText.MSG_END_DT.getDisplayText (aDisplayLocale)).setDisplayType (EDTColType.DATETIME,
                                                                                                                     aDisplayLocale),
                                        new DTCol (EText.MSG_DURATION.getDisplayText (aDisplayLocale)),
                                        new DTCol (EText.MSG_USER.getDisplayText (aDisplayLocale)),
                                        new DTCol (EText.MSG_SUCCESS.getDisplayText (aDisplayLocale))).setID (getID ());

    // The currently running jobs are not part of the result manager
    for (final LongRunningJobData aItem : m_aJobMgr.getAllRunningJobs ())
      _addRow (aWPEC, aTable, aItem, true);
    for (final LongRunningJobData aItem : aAllResults)
      _addRow (aWPEC, aTable, aItem, false);

    aNodeList.addChild (aTable);

    final DataTables aDataTables = BootstrapDataTables.createDefaultDataTables (aWPEC, aTable);
    aNodeList.addChild (aDataTables);
  }
}
