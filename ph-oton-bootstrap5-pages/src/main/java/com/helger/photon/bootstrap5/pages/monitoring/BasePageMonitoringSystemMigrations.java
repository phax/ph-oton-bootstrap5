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

import java.util.Locale;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import com.helger.annotation.Nonempty;
import com.helger.annotation.misc.Translatable;
import com.helger.base.compare.ESortOrder;
import com.helger.base.enforce.ValueEnforcer;
import com.helger.datetime.format.PDTToString;
import com.helger.html.hc.html.tabular.HCRow;
import com.helger.html.hc.html.tabular.HCTable;
import com.helger.html.hc.impl.HCNodeList;
import com.helger.photon.bootstrap5.buttongroup.BootstrapButtonToolbar;
import com.helger.photon.bootstrap5.pages.AbstractBootstrapWebPage;
import com.helger.photon.bootstrap5.uictrls.datatables.BootstrapDataTables;
import com.helger.photon.core.EPhotonCoreText;
import com.helger.photon.core.sysmigration.SystemMigrationManager;
import com.helger.photon.core.sysmigration.SystemMigrationResult;
import com.helger.photon.uicore.icon.EDefaultIcon;
import com.helger.photon.uicore.page.EWebPageText;
import com.helger.photon.uicore.page.IWebPageExecutionContext;
import com.helger.photon.uictrls.datatables.DataTables;
import com.helger.photon.uictrls.datatables.column.DTCol;
import com.helger.photon.uictrls.datatables.column.EDTColType;
import com.helger.text.IMultilingualText;
import com.helger.text.display.IHasDisplayText;
import com.helger.text.resolve.DefaultTextResolver;
import com.helger.text.util.TextHelper;

/**
 * Show system migration status.
 *
 * @author Philip Helger
 * @param <WPECTYPE>
 *        Web Page Execution Context type
 */
public class BasePageMonitoringSystemMigrations <WPECTYPE extends IWebPageExecutionContext> extends AbstractBootstrapWebPage <WPECTYPE>
{
  @Translatable
  protected enum EText implements IHasDisplayText
  {
    MSG_ID ("ID", "ID"),
    MSG_DATE ("Datum", "Date"),
    MSG_SUCCESS ("Erfolg?", "Success?"),
    MSG_ERRORMESSAGE ("Fehlermeldung", "Error message");

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

  private final SystemMigrationManager m_aSystemMigrationMgr;

  public BasePageMonitoringSystemMigrations (@NonNull @Nonempty final String sID, @NonNull final SystemMigrationManager aSystemMigrationMgr)
  {
    super (sID, EWebPageText.PAGE_NAME_MONITORING_SYSTEMMIGRATIONS.getAsMLT ());
    m_aSystemMigrationMgr = ValueEnforcer.notNull (aSystemMigrationMgr, "SystemMigrationMgr");
  }

  public BasePageMonitoringSystemMigrations (@NonNull @Nonempty final String sID,
                                             @NonNull final String sName,
                                             @NonNull final SystemMigrationManager aSystemMigrationMgr)
  {
    super (sID, sName);
    m_aSystemMigrationMgr = ValueEnforcer.notNull (aSystemMigrationMgr, "SystemMigrationMgr");
  }

  public BasePageMonitoringSystemMigrations (@NonNull @Nonempty final String sID,
                                             @NonNull final String sName,
                                             @Nullable final String sDescription,
                                             @NonNull final SystemMigrationManager aSystemMigrationMgr)
  {
    super (sID, sName, sDescription);
    m_aSystemMigrationMgr = ValueEnforcer.notNull (aSystemMigrationMgr, "SystemMigrationMgr");
  }

  public BasePageMonitoringSystemMigrations (@NonNull @Nonempty final String sID,
                                             @NonNull final IMultilingualText aName,
                                             @Nullable final IMultilingualText aDescription,
                                             @NonNull final SystemMigrationManager aSystemMigrationMgr)
  {
    super (sID, aName, aDescription);
    m_aSystemMigrationMgr = ValueEnforcer.notNull (aSystemMigrationMgr, "SystemMigrationMgr");
  }

  @NonNull
  protected final SystemMigrationManager getSystemMigrationMgr ()
  {
    return m_aSystemMigrationMgr;
  }

  @Override
  protected void fillContent (@NonNull final WPECTYPE aWPEC)
  {
    final HCNodeList aNodeList = aWPEC.getNodeList ();
    final Locale aDisplayLocale = aWPEC.getDisplayLocale ();

    // Refresh button
    final BootstrapButtonToolbar aToolbar = new BootstrapButtonToolbar (aWPEC);
    aToolbar.addButton (EPhotonCoreText.BUTTON_REFRESH.getDisplayText (aDisplayLocale), aWPEC.getSelfHref (), EDefaultIcon.REFRESH);
    aNodeList.addChild (aToolbar);

    final HCTable aTable = new HCTable (new DTCol (EText.MSG_ID.getDisplayText (aDisplayLocale)),
                                        new DTCol (EText.MSG_DATE.getDisplayText (aDisplayLocale)).setDisplayType (EDTColType.DATETIME,
                                                                                                                   aDisplayLocale)
                                                                                                  .setInitialSorting (ESortOrder.DESCENDING),
                                        new DTCol (EText.MSG_SUCCESS.getDisplayText (aDisplayLocale)).setDataSort (2, 1),
                                        new DTCol (EText.MSG_ERRORMESSAGE.getDisplayText (aDisplayLocale)).setDataSort (3, 2, 1))
                                                                                                                                 .setID (getID ());

    for (final SystemMigrationResult aItem : m_aSystemMigrationMgr.getAllMigrationResultsFlattened ())
    {
      final HCRow aRow = aTable.addBodyRow ();
      aRow.addCell (aItem.getID ());
      aRow.addCell (PDTToString.getAsString (aItem.getExecutionDateTime (), aDisplayLocale));
      aRow.addCell (EPhotonCoreText.getYesOrNo (aItem.isSuccess (), aDisplayLocale));
      aRow.addCell (aItem.getErrorMessage ());
    }
    aNodeList.addChild (aTable);

    final DataTables aDataTables = BootstrapDataTables.createDefaultDataTables (aWPEC, aTable);
    aNodeList.addChild (aDataTables);
  }
}
