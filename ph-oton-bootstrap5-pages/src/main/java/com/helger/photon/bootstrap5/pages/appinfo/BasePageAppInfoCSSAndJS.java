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

import java.util.Locale;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import com.helger.annotation.Nonempty;
import com.helger.annotation.misc.Translatable;
import com.helger.html.hc.config.HCSettings;
import com.helger.html.hc.html.HC_Target;
import com.helger.html.hc.html.script.HCScriptInline;
import com.helger.html.hc.html.tabular.HCCol;
import com.helger.html.hc.html.textlevel.HCA;
import com.helger.html.hc.impl.HCNodeList;
import com.helger.html.js.JSMarshaller;
import com.helger.html.js.UnparsedJSCodeProvider;
import com.helger.html.resource.css.ICSSPathProvider;
import com.helger.html.resource.js.IJSPathProvider;
import com.helger.photon.app.PhotonAppSettings;
import com.helger.photon.app.html.PhotonCSS;
import com.helger.photon.app.html.PhotonJS;
import com.helger.photon.bootstrap5.pages.AbstractBootstrapWebPage;
import com.helger.photon.bootstrap5.table.BootstrapTable;
import com.helger.photon.uicore.page.IWebPageExecutionContext;
import com.helger.text.IMultilingualText;
import com.helger.text.display.IHasDisplayText;
import com.helger.text.resolve.DefaultTextResolver;
import com.helger.text.util.TextHelper;
import com.helger.url.ISimpleURL;
import com.helger.web.scope.IRequestWebScopeWithoutResponse;

/**
 * Show all registered CSS and JS resources - the globally registered ones as well as the ones
 * actually included in the current page - in the order in which they are included.
 *
 * @author Philip Helger
 * @param <WPECTYPE>
 *        Web Page Execution Context type
 * @since 0.9.6
 */
public class BasePageAppInfoCSSAndJS <WPECTYPE extends IWebPageExecutionContext> extends
                                     AbstractBootstrapWebPage <WPECTYPE>
{
  @Translatable
  protected enum EText implements IHasDisplayText
  {
    PAGE_NAME ("CSS und JS", "CSS and JS"),
    MSG_GLOBAL ("Global registrierte Ressourcen", "Globally registered resources"),
    MSG_CURRENT_PAGE ("In dieser Seite enthaltene Ressourcen (inklusive der globalen und der Ressourcen dieser Anfrage)",
                      "Resources included in this page (including the global and the per-request resources)"),
    MSG_NONE ("Keine Ressourcen registriert", "No resources registered"),
    MSG_INDEX ("#", "#"),
    MSG_TYPE ("Typ", "Type"),
    MSG_PATH ("Pfad", "Path");

    @NonNull
    private final IMultilingualText m_aTP;

    EText (@NonNull final String sDE, @NonNull final String sEN)
    {
      m_aTP = TextHelper.create_DE_EN (sDE, sEN);
    }

    @NonNull
    public IMultilingualText getAsMLT ()
    {
      return m_aTP;
    }

    @Nullable
    public String getDisplayText (@NonNull final Locale aContentLocale)
    {
      return DefaultTextResolver.getTextStatic (this, m_aTP, aContentLocale);
    }
  }

  private static final String TYPE_CSS = "CSS";
  private static final String TYPE_JS = "JS";

  public BasePageAppInfoCSSAndJS (@NonNull @Nonempty final String sID)
  {
    super (sID, EText.PAGE_NAME.getAsMLT ());
  }

  public BasePageAppInfoCSSAndJS (@NonNull @Nonempty final String sID, @NonNull final String sName)
  {
    super (sID, sName);
  }

  public BasePageAppInfoCSSAndJS (@NonNull @Nonempty final String sID,
                                  @NonNull final String sName,
                                  @Nullable final String sDescription)
  {
    super (sID, sName, sDescription);
  }

  public BasePageAppInfoCSSAndJS (@NonNull @Nonempty final String sID,
                                  @NonNull final IMultilingualText aName,
                                  @Nullable final IMultilingualText aDescription)
  {
    super (sID, aName, aDescription);
  }

  @NonNull
  private static BootstrapTable _createTable (@NonNull final Locale aDisplayLocale)
  {
    final BootstrapTable ret = new BootstrapTable (new HCCol (50), new HCCol (70), HCCol.star ());
    ret.setStriped (true);
    ret.addHeaderRow ()
       .addCells (EText.MSG_INDEX.getDisplayText (aDisplayLocale),
                  EText.MSG_TYPE.getDisplayText (aDisplayLocale),
                  EText.MSG_PATH.getDisplayText (aDisplayLocale));
    return ret;
  }

  @NonNull
  private static HCA _createLink (@NonNull final ISimpleURL aURL)
  {
    // Open in a clean browser tab
    return new HCA (aURL).setTarget (HC_Target.BLANK).setRel ("noopener noreferrer").addChild (aURL.getAsString ());
  }

  @Override
  protected void fillContent (@NonNull final WPECTYPE aWPEC)
  {
    final HCNodeList aNodeList = aWPEC.getNodeList ();
    final Locale aDisplayLocale = aWPEC.getDisplayLocale ();
    final IRequestWebScopeWithoutResponse aRequestScope = aWPEC.getRequestScope ();
    final boolean bRegular = HCSettings.isUseRegularResources ();

    // Global resources - CSS is always included before JS
    {
      aNodeList.addChild (getUIHandler ().createDataGroupHeader (EText.MSG_GLOBAL.getDisplayText (aDisplayLocale)));

      final BootstrapTable aTable = _createTable (aDisplayLocale);
      int nIndex = 0;
      for (final ICSSPathProvider aCSS : PhotonCSS.getAllRegisteredCSSIncludesForGlobal ())
      {
        nIndex++;
        aTable.addBodyRow ()
              .addCell (Integer.toString (nIndex))
              .addCell (TYPE_CSS)
              .addCell (_createLink (PhotonAppSettings.getCSSPath (aRequestScope, aCSS, bRegular)));
      }
      for (final IJSPathProvider aJS : PhotonJS.getAllRegisteredJSIncludesForGlobal ())
      {
        nIndex++;
        aTable.addBodyRow ()
              .addCell (Integer.toString (nIndex))
              .addCell (TYPE_JS)
              .addCell (_createLink (PhotonAppSettings.getJSPath (aRequestScope, aJS, bRegular)));
      }
      if (nIndex == 0)
        aNodeList.addChild (info (EText.MSG_NONE.getDisplayText (aDisplayLocale)));
      else
        aNodeList.addChild (aTable);
    }

    // Current page resources
    // The per-request resources are only registered while the page is converted to HTML (after this
    // method), so the list is filled on the client side from the elements actually in the DOM
    {
      aNodeList.addChild (getUIHandler ().createDataGroupHeader (EText.MSG_CURRENT_PAGE.getDisplayText (aDisplayLocale)));

      final String sTableID = getID () + "-page";
      final BootstrapTable aTable = _createTable (aDisplayLocale);
      aTable.setID (sTableID);
      aNodeList.addChild (aTable);

      final String sJS = "var t=document.getElementById('" +
                         JSMarshaller.javaScriptEscape (sTableID) +
                         "');" +
                         "var b=t.tBodies.length>0?t.tBodies[0]:t.createTBody();" +
                         "document.querySelectorAll('link[rel=\"stylesheet\"][href],script[src]').forEach(function(e,i){" +
                         "var css=e.tagName==='LINK';" +
                         "var r=b.insertRow();" +
                         "r.insertCell().textContent=i+1;" +
                         "r.insertCell().textContent=css?'" +
                         TYPE_CSS +
                         "':'" +
                         TYPE_JS +
                         "';" +
                         "var a=document.createElement('a');" +
                         "a.href=css?e.href:e.src;" +
                         "a.target='_blank';" +
                         "a.rel='noopener noreferrer';" +
                         "a.textContent=e.getAttribute(css?'href':'src');" +
                         "r.insertCell().appendChild(a);" +
                         "});";
      aNodeList.addChild (new HCScriptInline (new UnparsedJSCodeProvider (sJS)));
    }
  }
}
