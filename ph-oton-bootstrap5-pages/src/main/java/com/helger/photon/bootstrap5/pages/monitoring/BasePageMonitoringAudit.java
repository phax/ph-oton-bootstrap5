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
package com.helger.photon.bootstrap5.pages.monitoring;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import com.helger.annotation.Nonempty;
import com.helger.annotation.Nonnegative;
import com.helger.annotation.concurrent.NotThreadSafe;
import com.helger.annotation.misc.Translatable;
import com.helger.annotation.style.OverrideOnDemand;
import com.helger.base.compare.ESortOrder;
import com.helger.base.enforce.ValueEnforcer;
import com.helger.base.string.StringHelper;
import com.helger.collection.commons.CommonsArrayList;
import com.helger.collection.commons.CommonsTreeMap;
import com.helger.collection.commons.ICommonsList;
import com.helger.collection.commons.ICommonsSortedMap;
import com.helger.datetime.format.PDTFromString;
import com.helger.datetime.format.PDTToString;
import com.helger.datetime.helper.PDTFactory;
import com.helger.html.hc.html.forms.HCEdit;
import com.helger.html.hc.html.forms.HCHiddenField;
import com.helger.html.hc.html.tabular.HCRow;
import com.helger.html.hc.html.tabular.HCTable;
import com.helger.html.hc.html.textlevel.HCA;
import com.helger.html.hc.impl.HCNodeList;
import com.helger.photon.audit.EAuditActionType;
import com.helger.photon.audit.IAuditItem;
import com.helger.photon.audit.IAuditManager;
import com.helger.photon.bootstrap5.CBootstrapCSS;
import com.helger.photon.bootstrap5.button.BootstrapButton;
import com.helger.photon.bootstrap5.button.BootstrapSubmitButton;
import com.helger.photon.bootstrap5.buttongroup.BootstrapButtonToolbar;
import com.helger.photon.bootstrap5.form.BootstrapForm;
import com.helger.photon.bootstrap5.form.BootstrapFormGroup;
import com.helger.photon.bootstrap5.nav.BootstrapNav;
import com.helger.photon.bootstrap5.nav.EBootstrapNavType;
import com.helger.photon.bootstrap5.pages.AbstractBootstrapWebPage;
import com.helger.photon.bootstrap5.table.BootstrapTable;
import com.helger.photon.bootstrap5.uictrls.datatables.BootstrapDataTables;
import com.helger.photon.bootstrap5.uictrls.datetimepicker.BootstrapDateTimePicker;
import com.helger.photon.core.EPhotonCoreText;
import com.helger.photon.core.form.RequestField;
import com.helger.photon.security.mgr.PhotonSecurityManager;
import com.helger.photon.security.user.IUser;
import com.helger.photon.security.util.SecurityHelper;
import com.helger.photon.uicore.html.select.AbstractHCExtSelect;
import com.helger.photon.uicore.html.select.HCExtSelect;
import com.helger.photon.uicore.icon.EDefaultIcon;
import com.helger.photon.uicore.page.EWebPageText;
import com.helger.photon.uicore.page.IWebPageExecutionContext;
import com.helger.photon.uictrls.datatables.DataTables;
import com.helger.photon.uictrls.datatables.column.DTCol;
import com.helger.photon.uictrls.datatables.column.EDTColType;
import com.helger.text.IMultilingualText;
import com.helger.text.display.IHasDisplayTextWithArgs;
import com.helger.text.resolve.DefaultTextResolver;
import com.helger.text.util.TextHelper;
import com.helger.url.SimpleURL;

/**
 * Show audit items. The page has three tabs: the latest audit items, all audit items of a single
 * date, and a search over the audit items of the last days. The latter two require an
 * {@link IAuditManager} that supports
 * {@link IAuditManager#getAllAuditItemsOfDateRange(LocalDate, LocalDate)}.
 *
 * @author Philip Helger
 * @param <WPECTYPE>
 *        Web page execution context type
 */
public class BasePageMonitoringAudit <WPECTYPE extends IWebPageExecutionContext> extends
                                     AbstractBootstrapWebPage <WPECTYPE>
{
  @Translatable
  protected enum EText implements IHasDisplayTextWithArgs
  {
    MSG_EARLIEST_DATA ("Älteste verfügbare Daten: ", "Earliest available data: "),
    MSG_DATE ("Datum", "Date"),
    MSG_USER ("Benutzer", "User"),
    MSG_TYPE ("Typ", "Type"),
    MSG_SUCCESS ("Erfolg?", "Success?"),
    MSG_ACTION ("Aktion", "Action"),
    TAB_LATEST ("Neueste Einträge", "Latest entries"),
    TAB_BY_DATE ("Einträge nach Datum", "Entries by date"),
    TAB_SEARCH ("Suche", "Search"),
    MSG_NOT_SUPPORTED ("Der verwendete Audit-Manager unterstützt keine Abfrage nach Datum.",
                       "The audit manager in use does not support querying by date."),
    MSG_PREV_DAY ("Vorheriger Tag", "Previous day"),
    MSG_NEXT_DAY ("Nächster Tag", "Next day"),
    MSG_TODAY ("Heute", "Today"),
    MSG_SHOW ("Anzeigen", "Show"),
    MSG_NO_ENTRIES_OF_DATE ("Für den {0} sind keine Einträge vorhanden.", "No entries are present for {0}."),
    MSG_ENTRIES_OF_DATE ("{0} Einträge vom {1}", "{0} entries of {1}"),
    MSG_RESULT_CAPPED ("Es werden nur die neuesten {0} von {1} Einträgen angezeigt.",
                       "Only the most recent {0} of {1} entries are shown."),
    MSG_SEARCH_TEXT ("Suchtext", "Search text"),
    MSG_SEARCH_TEXT_HELP ("Durchsucht Aktion, Benutzer-ID, Benutzername und Typ ohne Berücksichtigung der Groß-/Kleinschreibung",
                          "Searches action, user ID, user name and type, ignoring case"),
    MSG_SEARCH_DAYS ("Zeitraum", "Period"),
    MSG_LAST_DAYS ("Letzte {0} Tage", "Last {0} days"),
    MSG_TODAY_ONLY ("Nur heute", "Today only"),
    MSG_ALL ("Alle", "All"),
    MSG_SUCCESS_ONLY ("Nur erfolgreiche", "Successful only"),
    MSG_FAILURE_ONLY ("Nur fehlgeschlagene", "Failed only"),
    MSG_SEARCH_BUTTON ("Suchen", "Search"),
    MSG_SEARCH_RESULT ("{0} passende Einträge im Zeitraum von {1} bis {2}",
                       "{0} matching entries in the period from {1} to {2}"),
    MSG_SUMMARY_PER_DAY ("Zusammenfassung pro Tag", "Summary per day"),
    MSG_TOTAL ("Gesamt", "Total"),
    MSG_SUCCESSFUL ("Erfolgreich", "Successful"),
    MSG_FAILED ("Fehlgeschlagen", "Failed");

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

  @NotThreadSafe
  private static final class Counts
  {
    private int m_nSuccess = 0;
    private int m_nError = 0;

    int getSuccess ()
    {
      return m_nSuccess;
    }

    void incSuccess ()
    {
      m_nSuccess++;
    }

    int getError ()
    {
      return m_nError;
    }

    public boolean hasError ()
    {
      return m_nError > 0;
    }

    void incError ()
    {
      m_nError++;
    }

    int getSum ()
    {
      return m_nSuccess + m_nError;
    }
  }

  public static final int DEFAULT_MAX_ITEMS = 250;
  public static final String PARAM_MAX_ITEMS = "maxitems";

  /** The request parameter that selects the tab */
  public static final String PARAM_TAB = "tab";
  public static final String TABID_LATEST = "latest";
  public static final String TABID_BY_DATE = "date";
  public static final String TABID_SEARCH = "search";

  /** The date to show in the "by date" tab, in the format of the display locale */
  public static final String PARAM_DATE = "date";

  /** The number of days, including today, to search */
  public static final String PARAM_SEARCH_DAYS = "days";
  public static final String PARAM_SEARCH_TEXT = "text";
  public static final String PARAM_SEARCH_USER = "user";
  public static final String PARAM_SEARCH_TYPE = "type";
  public static final String PARAM_SEARCH_SUCCESS = "success";
  public static final String SEARCH_SUCCESS_TRUE = "true";
  public static final String SEARCH_SUCCESS_FALSE = "false";

  public static final int DEFAULT_SEARCH_DAYS = 7;
  public static final int MAX_SEARCH_DAYS = 30;
  private static final int [] SEARCH_DAYS_OPTIONS = { 1, 3, 7, 14, MAX_SEARCH_DAYS };

  /** The maximum number of rows shown in the "by date" and "search" tabs */
  public static final int DEFAULT_MAX_RESULT_ITEMS = 5_000;

  private final IAuditManager m_aAuditMgr;

  public BasePageMonitoringAudit (@NonNull @Nonempty final String sID, @NonNull final IAuditManager aAuditMgr)
  {
    super (sID, EWebPageText.PAGE_NAME_MONITORING_AUDIT.getAsMLT ());
    m_aAuditMgr = ValueEnforcer.notNull (aAuditMgr, "AuditManager");
  }

  public BasePageMonitoringAudit (@NonNull @Nonempty final String sID,
                                  @NonNull final String sName,
                                  @NonNull final IAuditManager aAuditMgr)
  {
    super (sID, sName);
    m_aAuditMgr = ValueEnforcer.notNull (aAuditMgr, "AuditManager");
  }

  public BasePageMonitoringAudit (@NonNull @Nonempty final String sID,
                                  @NonNull final String sName,
                                  @Nullable final String sDescription,
                                  @NonNull final IAuditManager aAuditMgr)
  {
    super (sID, sName, sDescription);
    m_aAuditMgr = ValueEnforcer.notNull (aAuditMgr, "AuditManager");
  }

  public BasePageMonitoringAudit (@NonNull @Nonempty final String sID,
                                  @NonNull final IMultilingualText aName,
                                  @Nullable final IMultilingualText aDescription,
                                  @NonNull final IAuditManager aAuditMgr)
  {
    super (sID, aName, aDescription);
    m_aAuditMgr = ValueEnforcer.notNull (aAuditMgr, "AuditManager");
  }

  @NonNull
  protected final IAuditManager getAuditMgr ()
  {
    return m_aAuditMgr;
  }

  @NonNull
  @OverrideOnDemand
  protected String getActionString (@NonNull final IAuditItem aItem)
  {
    return aItem.getAction ();
  }

  /**
   * @return The maximum number of rows shown in the "by date" and "search" tabs. If more items
   *         match, only the most recent ones are shown. Must be &gt; 0.
   */
  @Nonnegative
  @OverrideOnDemand
  protected int getMaxResultItems ()
  {
    return DEFAULT_MAX_RESULT_ITEMS;
  }

  @NonNull
  private static String _getDateString (@NonNull final LocalDate aDate, @NonNull final Locale aDisplayLocale)
  {
    return PDTToString.getAsString (aDate, aDisplayLocale);
  }

  @NonNull
  private SimpleURL _getByDateURL (@NonNull final WPECTYPE aWPEC, @NonNull final LocalDate aDate)
  {
    return aWPEC.getSelfHref ()
                .add (PARAM_TAB, TABID_BY_DATE)
                .add (PARAM_DATE, _getDateString (aDate, aWPEC.getDisplayLocale ()));
  }

  @NonNull
  private HCNodeList _createItemTable (@NonNull final WPECTYPE aWPEC, @NonNull final List <IAuditItem> aItems)
  {
    final Locale aDisplayLocale = aWPEC.getDisplayLocale ();
    final HCNodeList ret = new HCNodeList ();

    final HCTable aTable = new HCTable (new DTCol (EText.MSG_DATE.getDisplayText (aDisplayLocale)).setDisplayType (EDTColType.DATETIME,
                                                                                                                   aDisplayLocale)
                                                                                                  .setInitialSorting (ESortOrder.DESCENDING),
                                        new DTCol (EText.MSG_USER.getDisplayText (aDisplayLocale)),
                                        new DTCol (EText.MSG_TYPE.getDisplayText (aDisplayLocale)),
                                        new DTCol (EText.MSG_SUCCESS.getDisplayText (aDisplayLocale)),
                                        new DTCol (EText.MSG_ACTION.getDisplayText (aDisplayLocale)).setDataSort (4, 0))
                                                                                                                        .setID (getID ());
    for (final IAuditItem aItem : aItems)
    {
      final HCRow aRow = aTable.addBodyRow ();
      if (aItem.getSuccess ().isFailure ())
        aRow.addClass (CBootstrapCSS.TABLE_DANGER);
      aRow.addCell (PDTToString.getAsString (aItem.getDateTime (), aDisplayLocale));
      aRow.addCell (SecurityHelper.getUserDisplayName (aItem.getUserID (), aDisplayLocale));
      aRow.addCell (aItem.getType ().getID ());
      aRow.addCell (EPhotonCoreText.getYesOrNo (aItem.getSuccess ().isSuccess (), aDisplayLocale));
      aRow.addCell (getActionString (aItem));
    }
    ret.addChild (aTable);

    final DataTables aDataTables = BootstrapDataTables.createDefaultDataTables (aWPEC, aTable);
    ret.addChild (aDataTables);
    return ret;
  }

  /**
   * Show at most {@link #getMaxResultItems()} items. If there are more, the most recent ones are
   * shown together with a warning.
   */
  private void _addCappedItemTable (@NonNull final WPECTYPE aWPEC, @NonNull final List <IAuditItem> aItemsAsc)
  {
    final HCNodeList aNodeList = aWPEC.getNodeList ();
    final int nMaxItems = getMaxResultItems ();
    final int nTotal = aItemsAsc.size ();
    List <IAuditItem> aShownItems = aItemsAsc;
    if (nTotal > nMaxItems)
    {
      aNodeList.addChild (warn (EText.MSG_RESULT_CAPPED.getDisplayTextWithArgs (aWPEC.getDisplayLocale (),
                                                                                Integer.toString (nMaxItems),
                                                                                Integer.toString (nTotal))));
      aShownItems = aItemsAsc.subList (nTotal - nMaxItems, nTotal);
    }
    aNodeList.addChild (_createItemTable (aWPEC, aShownItems));
  }

  private void _showLatest (@NonNull final WPECTYPE aWPEC)
  {
    // Check max items parameter
    int nMaxItems = aWPEC.params ().getAsInt (PARAM_MAX_ITEMS);
    if (nMaxItems <= 0)
      nMaxItems = DEFAULT_MAX_ITEMS;

    aWPEC.getNodeList ().addChild (_createItemTable (aWPEC, m_aAuditMgr.getLastAuditItems (nMaxItems)));
  }

  private void _showByDate (@NonNull final WPECTYPE aWPEC, @Nullable final LocalDate aEarliestDate)
  {
    final HCNodeList aNodeList = aWPEC.getNodeList ();
    final Locale aDisplayLocale = aWPEC.getDisplayLocale ();
    final LocalDate aToday = PDTFactory.getCurrentLocalDate ();

    LocalDate aDate = PDTFromString.getLocalDateFromString (aWPEC.params ().getAsStringTrimmed (PARAM_DATE),
                                                            aDisplayLocale);
    if (aDate == null || aDate.isAfter (aToday))
      aDate = aToday;

    // Day navigation
    {
      final BootstrapForm aForm = aNodeList.addAndReturnChild (getUIHandler ().createFormSelf (aWPEC));
      aForm.addChild (new HCHiddenField (PARAM_TAB, TABID_BY_DATE));

      final BootstrapDateTimePicker aDTP = BootstrapDateTimePicker.create (PARAM_DATE, aDate, aDisplayLocale);
      aDTP.setMinDate (aEarliestDate);
      aDTP.setMaxDate (aToday);
      aForm.addFormGroup (new BootstrapFormGroup ().setLabel (EText.MSG_DATE.getDisplayText (aDisplayLocale))
                                                   .setCtrl (aDTP));

      final BootstrapButtonToolbar aToolbar = aForm.addAndReturnChild (new BootstrapButtonToolbar (aWPEC));
      aToolbar.addChild (new BootstrapSubmitButton ().addChild (EText.MSG_SHOW.getDisplayText (aDisplayLocale))
                                                     .setIcon (EDefaultIcon.MAGNIFIER));
      final LocalDate aPrevDate = aDate.minusDays (1);
      aToolbar.addChild (new BootstrapButton ().addChild (EText.MSG_PREV_DAY.getDisplayText (aDisplayLocale))
                                               .setIcon (EDefaultIcon.BACK)
                                               .setOnClick (_getByDateURL (aWPEC, aPrevDate))
                                               .setDisabled (aEarliestDate != null &&
                                                             aPrevDate.isBefore (aEarliestDate)));
      final LocalDate aNextDate = aDate.plusDays (1);
      aToolbar.addChild (new BootstrapButton ().addChild (EText.MSG_NEXT_DAY.getDisplayText (aDisplayLocale))
                                               .setIcon (EDefaultIcon.FORWARD)
                                               .setOnClick (_getByDateURL (aWPEC, aNextDate))
                                               .setDisabled (aNextDate.isAfter (aToday)));
      aToolbar.addChild (new BootstrapButton ().addChild (EText.MSG_TODAY.getDisplayText (aDisplayLocale))
                                               .setOnClick (_getByDateURL (aWPEC, aToday))
                                               .setDisabled (aDate.equals (aToday)));
    }

    final List <IAuditItem> aItems = m_aAuditMgr.getAllAuditItemsOfDateRange (aDate, aDate);
    if (aItems == null)
    {
      aNodeList.addChild (info (EText.MSG_NOT_SUPPORTED.getDisplayText (aDisplayLocale)));
      return;
    }

    final String sDate = _getDateString (aDate, aDisplayLocale);
    if (aItems.isEmpty ())
      aNodeList.addChild (info (EText.MSG_NO_ENTRIES_OF_DATE.getDisplayTextWithArgs (aDisplayLocale, sDate)));
    else
    {
      aNodeList.addChild (div (EText.MSG_ENTRIES_OF_DATE.getDisplayTextWithArgs (aDisplayLocale,
                                                                                 Integer.toString (aItems.size ()),
                                                                                 sDate)).addClass (CBootstrapCSS.MB_3));
      _addCappedItemTable (aWPEC, aItems);
    }
  }

  private static boolean _containsIgnoreCase (@Nullable final String sValue, @NonNull final String sSearchLC)
  {
    return sValue != null && sValue.toLowerCase (Locale.ROOT).contains (sSearchLC);
  }

  private void _showSearch (@NonNull final WPECTYPE aWPEC)
  {
    final HCNodeList aNodeList = aWPEC.getNodeList ();
    final Locale aDisplayLocale = aWPEC.getDisplayLocale ();
    final LocalDate aToday = PDTFactory.getCurrentLocalDate ();

    int nDays = aWPEC.params ().getAsInt (PARAM_SEARCH_DAYS, DEFAULT_SEARCH_DAYS);
    if (nDays <= 0)
      nDays = DEFAULT_SEARCH_DAYS;
    else
      if (nDays > MAX_SEARCH_DAYS)
        nDays = MAX_SEARCH_DAYS;
    final String sSearchText = aWPEC.params ().getAsStringTrimmed (PARAM_SEARCH_TEXT);
    final String sSearchUserID = aWPEC.params ().getAsStringTrimmed (PARAM_SEARCH_USER);
    final EAuditActionType eSearchType = EAuditActionType.getFromIDOrNull (aWPEC.params ()
                                                                                .getAsStringTrimmed (PARAM_SEARCH_TYPE));
    final String sSearchSuccess = aWPEC.params ().getAsStringTrimmed (PARAM_SEARCH_SUCCESS);

    // Search form
    {
      final BootstrapForm aForm = aNodeList.addAndReturnChild (getUIHandler ().createFormSelf (aWPEC));
      aForm.addChild (new HCHiddenField (PARAM_TAB, TABID_SEARCH));

      final HCExtSelect aDaysSelect = new HCExtSelect (new RequestField (PARAM_SEARCH_DAYS,
                                                                         Integer.toString (DEFAULT_SEARCH_DAYS)));
      for (final int n : SEARCH_DAYS_OPTIONS)
        aDaysSelect.addOption (Integer.toString (n),
                               n == 1 ? EText.MSG_TODAY_ONLY.getDisplayText (aDisplayLocale)
                                      : EText.MSG_LAST_DAYS.getDisplayTextWithArgs (aDisplayLocale,
                                                                                    Integer.toString (n)));
      aForm.addFormGroup (new BootstrapFormGroup ().setLabel (EText.MSG_SEARCH_DAYS.getDisplayText (aDisplayLocale))
                                                   .setCtrl (aDaysSelect));

      aForm.addFormGroup (new BootstrapFormGroup ().setLabel (EText.MSG_SEARCH_TEXT.getDisplayText (aDisplayLocale))
                                                   .setCtrl (new HCEdit (new RequestField (PARAM_SEARCH_TEXT)))
                                                   .setHelpText (EText.MSG_SEARCH_TEXT_HELP.getDisplayText (aDisplayLocale)));

      final HCExtSelect aUserSelect = new HCExtSelect (new RequestField (PARAM_SEARCH_USER));
      final ICommonsList <IUser> aUsers = PhotonSecurityManager.getUserMgr ().getAll ();
      aUsers.sort (Comparator.comparing (x -> SecurityHelper.getUserDisplayName (x, aDisplayLocale)));
      for (final IUser aUser : aUsers)
        aUserSelect.addOption (aUser.getID (), SecurityHelper.getUserDisplayName (aUser, aDisplayLocale));
      aUserSelect.addOptionAt (0,
                               AbstractHCExtSelect.createSpecialOption (EText.MSG_ALL.getDisplayText (aDisplayLocale)));
      aForm.addFormGroup (new BootstrapFormGroup ().setLabel (EText.MSG_USER.getDisplayText (aDisplayLocale))
                                                   .setCtrl (aUserSelect));

      final HCExtSelect aTypeSelect = new HCExtSelect (new RequestField (PARAM_SEARCH_TYPE));
      aTypeSelect.addOptionAt (0,
                               AbstractHCExtSelect.createSpecialOption (EText.MSG_ALL.getDisplayText (aDisplayLocale)));
      for (final EAuditActionType eType : EAuditActionType.values ())
        aTypeSelect.addOption (eType.getID (), eType.getID ());
      aForm.addFormGroup (new BootstrapFormGroup ().setLabel (EText.MSG_TYPE.getDisplayText (aDisplayLocale))
                                                   .setCtrl (aTypeSelect));

      final HCExtSelect aSuccessSelect = new HCExtSelect (new RequestField (PARAM_SEARCH_SUCCESS));
      aSuccessSelect.addOptionAt (0,
                                  AbstractHCExtSelect.createSpecialOption (EText.MSG_ALL.getDisplayText (aDisplayLocale)));
      aSuccessSelect.addOption (SEARCH_SUCCESS_TRUE, EText.MSG_SUCCESS_ONLY.getDisplayText (aDisplayLocale));
      aSuccessSelect.addOption (SEARCH_SUCCESS_FALSE, EText.MSG_FAILURE_ONLY.getDisplayText (aDisplayLocale));
      aForm.addFormGroup (new BootstrapFormGroup ().setLabel (EText.MSG_SUCCESS.getDisplayText (aDisplayLocale))
                                                   .setCtrl (aSuccessSelect));

      aForm.addChild (new BootstrapSubmitButton ().addChild (EText.MSG_SEARCH_BUTTON.getDisplayText (aDisplayLocale))
                                                  .setIcon (EDefaultIcon.MAGNIFIER));
    }

    // Search is only executed if the form was submitted
    if (!aWPEC.params ().containsKey (PARAM_SEARCH_DAYS))
      return;

    final LocalDate aStartDate = aToday.minusDays (nDays - 1L);
    final List <IAuditItem> aAllItems = m_aAuditMgr.getAllAuditItemsOfDateRange (aStartDate, aToday);
    if (aAllItems == null)
    {
      aNodeList.addChild (info (EText.MSG_NOT_SUPPORTED.getDisplayText (aDisplayLocale)));
      return;
    }

    // Filter
    final String sSearchTextLC = StringHelper.isEmpty (sSearchText) ? null : sSearchText.toLowerCase (Locale.ROOT);
    final ICommonsList <IAuditItem> aMatchingItems = new CommonsArrayList <> ();
    for (final IAuditItem aItem : aAllItems)
    {
      if (StringHelper.isNotEmpty (sSearchUserID) && !sSearchUserID.equals (aItem.getUserID ()))
        continue;
      if (eSearchType != null && eSearchType != aItem.getType ())
        continue;
      if (SEARCH_SUCCESS_TRUE.equals (sSearchSuccess) && aItem.getSuccess ().isFailure ())
        continue;
      if (SEARCH_SUCCESS_FALSE.equals (sSearchSuccess) && aItem.getSuccess ().isSuccess ())
        continue;
      if (sSearchTextLC != null &&
          !_containsIgnoreCase (getActionString (aItem), sSearchTextLC) &&
          !_containsIgnoreCase (aItem.getUserID (), sSearchTextLC) &&
          !_containsIgnoreCase (SecurityHelper.getUserDisplayName (aItem.getUserID (), aDisplayLocale),
                                sSearchTextLC) &&
          !_containsIgnoreCase (aItem.getTypeID (), sSearchTextLC))
        continue;
      aMatchingItems.add (aItem);
    }

    aNodeList.addChild (div (EText.MSG_SEARCH_RESULT.getDisplayTextWithArgs (aDisplayLocale,
                                                                             Integer.toString (aMatchingItems.size ()),
                                                                             _getDateString (aStartDate,
                                                                                             aDisplayLocale),
                                                                             _getDateString (aToday, aDisplayLocale)))
                                                                                                                      .addClass (CBootstrapCSS.MB_3));

    // Summary per day - all days of the period, newest first
    {
      final ICommonsSortedMap <LocalDate, Counts> aCountPerDay = new CommonsTreeMap <> (Comparator.reverseOrder ());
      LocalDate aDate = aStartDate;
      while (!aDate.isAfter (aToday))
      {
        // index 0: success, index 1: failure
        aCountPerDay.put (aDate, new Counts ());
        aDate = aDate.plusDays (1);
      }
      for (final IAuditItem aItem : aMatchingItems)
      {
        final Counts aCounts = aCountPerDay.get (aItem.getDateTime ().toLocalDate ());
        if (aCounts != null)
          if (aItem.getSuccess ().isSuccess ())
            aCounts.incSuccess ();
          else
            aCounts.incError ();
      }

      aNodeList.addChild (h3 (EText.MSG_SUMMARY_PER_DAY.getDisplayText (aDisplayLocale)));
      final BootstrapTable aSummaryTable = new BootstrapTable ();
      aSummaryTable.addHeaderRow ()
                   .addCells (EText.MSG_DATE.getDisplayText (aDisplayLocale),
                              EText.MSG_TOTAL.getDisplayText (aDisplayLocale),
                              EText.MSG_SUCCESSFUL.getDisplayText (aDisplayLocale),
                              EText.MSG_FAILED.getDisplayText (aDisplayLocale));
      aCountPerDay.forEach ((aDay, aCounts) -> {
        final HCRow aRow = aSummaryTable.addBodyRow ();
        if (aCounts.hasError ())
          aRow.addClass (CBootstrapCSS.TABLE_DANGER);
        aRow.addCell (new HCA (_getByDateURL (aWPEC, aDay)).addChild (_getDateString (aDay, aDisplayLocale)));
        aRow.addCell (Integer.toString (aCounts.getSum ()));
        aRow.addCell (Integer.toString (aCounts.getSuccess ()));
        aRow.addCell (Integer.toString (aCounts.getError ()));
      });
      aNodeList.addChild (aSummaryTable);
    }

    if (aMatchingItems.isNotEmpty ())
      _addCappedItemTable (aWPEC, aMatchingItems);
  }

  @Override
  protected void fillContent (@NonNull final WPECTYPE aWPEC)
  {
    final HCNodeList aNodeList = aWPEC.getNodeList ();
    final Locale aDisplayLocale = aWPEC.getDisplayLocale ();

    String sTabID = aWPEC.params ().getAsStringTrimmed (PARAM_TAB);
    if (!TABID_BY_DATE.equals (sTabID) && !TABID_SEARCH.equals (sTabID))
      sTabID = TABID_LATEST;

    // Refresh button
    final BootstrapButtonToolbar aToolbar = new BootstrapButtonToolbar (aWPEC);
    aToolbar.addButton (EPhotonCoreText.BUTTON_REFRESH.getDisplayText (aDisplayLocale),
                        aWPEC.getSelfHref (),
                        EDefaultIcon.REFRESH);
    aNodeList.addChild (aToolbar);

    // Info
    final LocalDate aEarliestDate = m_aAuditMgr.getEarliestAuditDate ();
    aNodeList.addChild (div (EText.MSG_EARLIEST_DATA.getDisplayText (aDisplayLocale)).addChild (PDTToString.getAsString (aEarliestDate,
                                                                                                                         aDisplayLocale)));

    // Tabs - only the content of the active tab is created
    final BootstrapNav aNav = new BootstrapNav (EBootstrapNavType.TABS);
    aNav.addClass (CBootstrapCSS.MY_3);
    aNav.addItem ()
        .addNavLink (aWPEC.getSelfHref ().add (PARAM_TAB, TABID_LATEST))
        .setActive (TABID_LATEST.equals (sTabID))
        .addChild (EText.TAB_LATEST.getDisplayText (aDisplayLocale));
    aNav.addItem ()
        .addNavLink (aWPEC.getSelfHref ().add (PARAM_TAB, TABID_BY_DATE))
        .setActive (TABID_BY_DATE.equals (sTabID))
        .addChild (EText.TAB_BY_DATE.getDisplayText (aDisplayLocale));
    aNav.addItem ()
        .addNavLink (aWPEC.getSelfHref ().add (PARAM_TAB, TABID_SEARCH))
        .setActive (TABID_SEARCH.equals (sTabID))
        .addChild (EText.TAB_SEARCH.getDisplayText (aDisplayLocale));
    aNodeList.addChild (aNav);

    switch (sTabID)
    {
      case TABID_BY_DATE -> _showByDate (aWPEC, aEarliestDate);
      case TABID_SEARCH -> _showSearch (aWPEC);
      default -> _showLatest (aWPEC);
    }
  }
}
