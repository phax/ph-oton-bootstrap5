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
package com.helger.photon.bootstrap5.demo.pub.page;

import java.util.Locale;

import org.jspecify.annotations.NonNull;

import com.helger.css.property.CCSSProperties;
import com.helger.html.hc.IHCNode;
import com.helger.html.hc.html.forms.HCEdit;
import com.helger.html.hc.html.forms.HCTextArea;
import com.helger.html.hc.html.grouping.HCDiv;
import com.helger.html.hc.html.grouping.HCP;
import com.helger.html.hc.html.sections.HCH2;
import com.helger.html.hc.html.textlevel.HCA;
import com.helger.html.hc.html.textlevel.HCSmall;
import com.helger.html.hc.html.textlevel.HCSpan;
import com.helger.html.hc.impl.HCNodeList;
import com.helger.html.hc.impl.HCTextNode;
import com.helger.html.js.EJSEvent;
import com.helger.photon.bootstrap5.CBootstrapCSS;
import com.helger.photon.bootstrap5.alert.BootstrapErrorBox;
import com.helger.photon.bootstrap5.alert.BootstrapInfoBox;
import com.helger.photon.bootstrap5.alert.BootstrapSuccessBox;
import com.helger.photon.bootstrap5.alert.BootstrapWarnBox;
import com.helger.photon.bootstrap5.badge.BootstrapBadge;
import com.helger.photon.bootstrap5.badge.EBootstrapBadgeType;
import com.helger.photon.bootstrap5.breadcrumb.BootstrapBreadcrumb;
import com.helger.photon.bootstrap5.button.BootstrapButton;
import com.helger.photon.bootstrap5.button.EBootstrapButtonType;
import com.helger.photon.bootstrap5.buttongroup.BootstrapButtonGroup;
import com.helger.photon.bootstrap5.card.BootstrapCard;
import com.helger.photon.bootstrap5.demo.app.ui.AbstractAppWebPage;
import com.helger.photon.bootstrap5.dropdown.BootstrapDropdownMenu;
import com.helger.photon.bootstrap5.dropdown.EBootstrapDropType;
import com.helger.photon.bootstrap5.form.BootstrapFormFloating;
import com.helger.photon.bootstrap5.form.BootstrapInvalidFeedback;
import com.helger.photon.bootstrap5.form.BootstrapValidFeedback;
import com.helger.photon.bootstrap5.grid.BootstrapCol;
import com.helger.photon.bootstrap5.grid.BootstrapGridSpec;
import com.helger.photon.bootstrap5.grid.BootstrapRow;
import com.helger.photon.bootstrap5.inputgroup.BootstrapInputGroup;
import com.helger.photon.bootstrap5.listgroup.BootstrapListGroup;
import com.helger.photon.bootstrap5.modal.BootstrapModal;
import com.helger.photon.bootstrap5.modal.EBootstrapModalSize;
import com.helger.photon.bootstrap5.nav.BootstrapTabBox;
import com.helger.photon.bootstrap5.offcanvas.BootstrapOffcanvas;
import com.helger.photon.bootstrap5.offcanvas.EBootstrapOffcanvasPlacement;
import com.helger.photon.bootstrap5.tooltip.BootstrapTooltip;
import com.helger.photon.bootstrap5.uictrls.ext.BootstrapCardCollapsible;
import com.helger.photon.bootstrap5.uictrls.ext.BootstrapFileUpload;
import com.helger.photon.bootstrap5.uictrls.ext.BootstrapSimpleTooltip;
import com.helger.photon.bootstrap5.uictrls.prism.BootstrapPrismJS;
import com.helger.photon.bootstrap5.uictrls.select2.BootstrapSelect2;
import com.helger.photon.bootstrap5.uictrls.treeview.BootstrapTreeView;
import com.helger.photon.bootstrap5.uictrls.treeview.BootstrapTreeViewItem;
import com.helger.photon.bootstrap5.utils.BootstrapBlockquote;
import com.helger.photon.bootstrap5.utils.BootstrapCollapseHelper;
import com.helger.photon.core.form.RequestField;
import com.helger.photon.uicore.page.WebPageExecutionContext;
import com.helger.photon.uictrls.prism.EPrismLanguage;
import com.helger.tree.DefaultTree;
import com.helger.tree.DefaultTreeItem;
import com.helger.url.SimpleURL;

/**
 * Shows all the controls that have no dedicated demo page of their own - especially the ones that
 * rely on the native Bootstrap 5 JavaScript API and can therefore only be verified in a browser.
 *
 * @author Philip Helger
 */
public class PagePublicMiscControls extends AbstractAppWebPage
{
  public PagePublicMiscControls (final String sID)
  {
    super (sID, "Misc Controls");
  }

  @NonNull
  private static HCH2 _title (@NonNull final String sText)
  {
    return new HCH2 ().addChild (sText).addClass (CBootstrapCSS.MT_4);
  }

  @NonNull
  private static HCDiv _row (@NonNull final IHCNode... aNodes)
  {
    final HCDiv aDiv = new HCDiv ().addClasses (CBootstrapCSS.D_FLEX, CBootstrapCSS.GAP_2, CBootstrapCSS.MB_3);
    for (final IHCNode aNode : aNodes)
      aDiv.addChild (aNode);
    return aDiv;
  }

  private static void _addJSComponents (@NonNull final HCNodeList aNodeList)
  {
    aNodeList.addChild (_title ("Bootstrap 5 JavaScript components"));
    aNodeList.addChild (new HCP ().addChild ("All of these use the native Bootstrap 5 JS API - no jQuery plugins."));

    // Modal
    final BootstrapModal aModal = new BootstrapModal (EBootstrapModalSize.NORMAL);
    aModal.setHeader ("A modal dialog");
    aModal.setBody ("The modal is opened with bootstrap.Modal.getOrCreateInstance (...).show ().");
    aModal.setFooter (new BootstrapButton ().addChild ("Nothing to do here"));

    final BootstrapButton aModalButton = new BootstrapButton (EBootstrapButtonType.PRIMARY).addChild ("Open modal");
    aModalButton.setEventHandler (EJSEvent.CLICK, aModal.jsModalShow ());

    // Offcanvas
    final BootstrapOffcanvas aOffcanvas = new BootstrapOffcanvas (EBootstrapOffcanvasPlacement.END);
    aOffcanvas.setHeader ("An offcanvas");
    aOffcanvas.setBody ("Offcanvas is a new Bootstrap 5 component. The toggle works without custom JS code.");
    final BootstrapButton aOffcanvasButton = new BootstrapButton (EBootstrapButtonType.SECONDARY).addChild ("Open offcanvas");
    aOffcanvas.applyToggleTo (aOffcanvasButton);

    // Tooltip
    final BootstrapButton aTooltipButton = new BootstrapButton (EBootstrapButtonType.OUTLINE_PRIMARY).addChild ("Hover for a tooltip");
    final BootstrapTooltip aTooltip = new BootstrapTooltip (aTooltipButton).setTooltipTitle ("Created with new bootstrap.Tooltip (...)");

    // Collapse
    final BootstrapButton aCollapseButton = new BootstrapButton (EBootstrapButtonType.OUTLINE_SECONDARY).addChild ("Toggle collapse");
    final HCDiv aCollapsible = new HCDiv ().addChild ("This block is shown and hidden by the Bootstrap 5 collapse plugin.")
                                           .addClasses (CBootstrapCSS.BORDER, CBootstrapCSS.P_3, CBootstrapCSS.MB_3);
    BootstrapCollapseHelper.makeCollapsible (aCollapseButton, aCollapsible);

    aNodeList.addChild (_row (aModalButton, aOffcanvasButton, aTooltipButton, aCollapseButton));
    aNodeList.addChild (aCollapsible);
    aNodeList.addChild (aModal);
    aNodeList.addChild (aOffcanvas);
    aNodeList.addChild (aTooltip);
  }

  private static void _addFormControls (@NonNull final HCNodeList aNodeList)
  {
    aNodeList.addChild (_title ("Form controls"));

    final BootstrapRow aRow = aNodeList.addAndReturnChild (new BootstrapRow ());

    // Floating labels
    {
      final BootstrapCol aCol = aRow.createColumn (BootstrapGridSpec.builder ().xs (12).md (6).build ());
      aCol.addChild (new HCP ().addChild (new HCSmall ().addChild ("Floating labels")));
      aCol.addChild (new BootstrapFormFloating (new HCEdit ("floating1"), "E-Mail address").addClass (
                                                                                                      CBootstrapCSS.MB_2));
      aCol.addChild (new BootstrapFormFloating (new HCTextArea ("floating2"), "A comment"));
    }

    // Validation feedback
    {
      final BootstrapCol aCol = aRow.createColumn (BootstrapGridSpec.builder ().xs (12).md (6).build ());
      aCol.addChild (new HCP ().addChild (new HCSmall ().addChild ("Validation feedback")));

      final HCEdit aValid = new HCEdit ("valid").addClasses (CBootstrapCSS.FORM_CONTROL, CBootstrapCSS.IS_VALID);
      aCol.addChild (aValid);
      aCol.addChild (new BootstrapValidFeedback ().addChild ("Looks good."));

      final HCEdit aInvalid = new HCEdit ("invalid").addClasses (CBootstrapCSS.FORM_CONTROL, CBootstrapCSS.IS_INVALID);
      aCol.addChild (aInvalid.addClass (CBootstrapCSS.MT_2));
      aCol.addChild (new BootstrapInvalidFeedback ().addChild ("Please provide a value."));
    }

    // Input group
    aNodeList.addChild (new HCP ().addChild (new HCSmall ().addChild ("Input group with prefix, suffix and button")));
    final BootstrapInputGroup aIG = new BootstrapInputGroup ();
    aIG.addChildPrefix ("@");
    aIG.addChild (new HCEdit ("inputgroup"));
    aIG.addChildSuffix (".com");
    aIG.addChildSuffix (new BootstrapButton (EBootstrapButtonType.OUTLINE_SECONDARY).addChild ("Check"));
    aNodeList.addChild (aIG);
  }

  private static void _addContentComponents (@NonNull final HCNodeList aNodeList)
  {
    aNodeList.addChild (_title ("Content components"));

    // Cards
    final BootstrapRow aCardRow = aNodeList.addAndReturnChild (new BootstrapRow ());
    {
      final BootstrapCol aCol = aCardRow.createColumn (BootstrapGridSpec.builder ().xs (12).md (6).build ());
      final BootstrapCard aCard = aCol.addAndReturnChild (new BootstrapCard ());
      aCard.createAndAddHeader ().addChild ("A card");
      aCard.createAndAddBody ().addChild ("Cards replace the Bootstrap 3 panels, wells and thumbnails.");
      aCard.createAndAddFooter ().addChild (new HCSmall ().addChild ("Card footer"));
    }
    {
      final BootstrapCol aCol = aCardRow.createColumn (BootstrapGridSpec.builder ().xs (12).md (6).build ());
      final BootstrapCardCollapsible aOpen = aCol.addAndReturnChild (new BootstrapCardCollapsible (new HCTextNode ("A collapsible card - initially open"),
                                                                                                   true));
      aOpen.getBody ().addChild ("Clicking the header collapses this card. The chevron is rotated by CSS.");

      final BootstrapCardCollapsible aClosed = aCol.addAndReturnChild (new BootstrapCardCollapsible (new HCTextNode ("... and one that starts collapsed"),
                                                                                                     false));
      aClosed.getBody ().addChild ("Content of the initially collapsed card.");
    }

    // Alerts
    aNodeList.addChild (new HCP ().addChild (new HCSmall ().addChild ("Alerts - the last one is dismissible")));
    aNodeList.addChild (new BootstrapInfoBox ().addChild ("An info box"));
    aNodeList.addChild (new BootstrapSuccessBox ().addChild ("A success box"));
    aNodeList.addChild (new BootstrapWarnBox ().addChild ("A warning box"));
    aNodeList.addChild (new BootstrapErrorBox ().setShowClose (true).addChild ("An error box that can be closed"));

    // Badges
    aNodeList.addChild (_row (new BootstrapBadge (EBootstrapBadgeType.PRIMARY).addChild ("Primary"),
                              new BootstrapBadge (EBootstrapBadgeType.SUCCESS).addChild ("Success"),
                              new BootstrapBadge (EBootstrapBadgeType.DANGER).setPill (true).addChild ("42"),
                              BootstrapBadge.createNumeric (7)));

    // Breadcrumb, list group and dropdown
    final BootstrapRow aRow = aNodeList.addAndReturnChild (new BootstrapRow ());
    {
      final BootstrapCol aCol = aRow.createColumn (BootstrapGridSpec.builder ().xs (12).md (4).build ());
      final BootstrapBreadcrumb aBC = aCol.addAndReturnChild (new BootstrapBreadcrumb ());
      aBC.getList ().addLink (new SimpleURL ("#"), "Home");
      aBC.getList ().addLink (new SimpleURL ("#"), "Library");
      aBC.getList ().addActive ("Data");
    }
    {
      final BootstrapCol aCol = aRow.createColumn (BootstrapGridSpec.builder ().xs (12).md (4).build ());
      final BootstrapListGroup aLG = aCol.addAndReturnChild (new BootstrapListGroup ());
      aLG.addItem ("First item");
      aLG.addItem ("Second item");
      aLG.addItem ("Third item");
    }
    {
      final BootstrapCol aCol = aRow.createColumn (BootstrapGridSpec.builder ().xs (12).md (4).build ());
      final BootstrapDropdownMenu aMenu = new BootstrapDropdownMenu ();
      aMenu.createAndAddHeader ().addChild ("Dropdown header");
      aMenu.createAndAddItem ().setHref (new SimpleURL ("#")).addChild ("An action");
      aMenu.createAndAddDivider ();
      aMenu.createAndAddItem ().setHref (new SimpleURL ("#")).addChild ("Another action");
      BootstrapDropdownMenu.assignMenuToButton (aCol,
                                                new BootstrapButton (EBootstrapButtonType.SECONDARY).addChild ("Dropdown"),
                                                aMenu,
                                                EBootstrapDropType.DROPDOWN,
                                                false);
    }

    // Button group
    final BootstrapButtonGroup aBG = new BootstrapButtonGroup ();
    aBG.addChild (new BootstrapButton (EBootstrapButtonType.OUTLINE_PRIMARY).addChild ("Left"));
    aBG.addChild (new BootstrapButton (EBootstrapButtonType.OUTLINE_PRIMARY).addChild ("Middle"));
    aBG.addChild (new BootstrapButton (EBootstrapButtonType.OUTLINE_PRIMARY).addChild ("Right"));
    aNodeList.addChild (_row (aBG));

    // Tab box
    final BootstrapTabBox aTabBox = new BootstrapTabBox ();
    aTabBox.addTab ("tab1",
                    new HCTextNode ("First tab"),
                    new HCP ().addChild ("Content of the first tab."),
                    true,
                    false);
    aTabBox.addTab ("tab2",
                    new HCTextNode ("Second tab"),
                    new HCP ().addChild ("Content of the second tab."),
                    false,
                    false);
    aTabBox.addTab ("tab3", new HCTextNode ("Disabled"), new HCP ().addChild ("Not reachable."), false, true);
    aNodeList.addChild (aTabBox);

    // Blockquote
    aNodeList.addChild (new BootstrapBlockquote ().addChild (new HCP ().addChild ("A quotation rendered as a Bootstrap 5 blockquote.")));
  }

  private static void _addUICtrls (@NonNull final HCNodeList aNodeList, @NonNull final Locale aDisplayLocale)
  {
    aNodeList.addChild (_title ("ph-oton UI controls"));

    // Simple tooltip
    aNodeList.addChild (new HCP ().addChild ("A label with a help icon ")
                                  .addChild (BootstrapSimpleTooltip.createSimpleTooltip ("This help text comes from BootstrapSimpleTooltip."))
                                  .addChild (" - hover the icon."));

    final BootstrapRow aRow = aNodeList.addAndReturnChild (new BootstrapRow ());

    // Tree view
    {
      final BootstrapCol aCol = aRow.createColumn (BootstrapGridSpec.builder ().xs (12).md (6).build ());
      aCol.addChild (new HCP ().addChild (new HCSmall ().addChild ("Tree view")));

      final DefaultTree <BootstrapTreeViewItem> aTree = new DefaultTree <> ();
      final DefaultTreeItem <BootstrapTreeViewItem> aRoot = aTree.getRootItem ();
      final DefaultTreeItem <BootstrapTreeViewItem> aParent1 = aRoot.createChildItem (new BootstrapTreeViewItem ("Fruits"));
      aParent1.createChildItem (new BootstrapTreeViewItem ("Apple"));
      aParent1.createChildItem (new BootstrapTreeViewItem ("Banana"));
      final DefaultTreeItem <BootstrapTreeViewItem> aParent2 = aRoot.createChildItem (new BootstrapTreeViewItem ("Vegetables"));
      aParent2.createChildItem (new BootstrapTreeViewItem ("Carrot"));
      aParent2.createChildItem (new BootstrapTreeViewItem ("Potato"));

      aCol.addChild (new BootstrapTreeView (aTree).setInitiallyExpanded (true).setShowExpandCollapseAllButtons (true));
    }

    // File upload and Select2
    {
      final BootstrapCol aCol = aRow.createColumn (BootstrapGridSpec.builder ().xs (12).md (6).build ());
      aCol.addChild (new HCP ().addChild (new HCSmall ().addChild ("File upload")));
      aCol.addChild (new BootstrapFileUpload ("file", aDisplayLocale));

      aCol.addChild (new HCP ().addChild (new HCSmall ().addChild ("Select2")).addClass (CBootstrapCSS.MT_3));
      final BootstrapSelect2 aSelect = new BootstrapSelect2 (new RequestField ("select2", "b"));
      // Select2 resolves its width from the style attribute of the select
      aSelect.addStyle (CCSSProperties.WIDTH.newValue ("100%"));
      aSelect.addOption ("a", "Alpha");
      aSelect.addOption ("b", "Beta");
      aSelect.addOption ("c", "Gamma");
      aCol.addChild (aSelect);
    }

    // Prism
    aNodeList.addChild (new HCP ().addChild (new HCSmall ().addChild ("Syntax highlighting with Prism.js")));
    aNodeList.addChild (new BootstrapPrismJS (EPrismLanguage.JAVA).addChild ("final BootstrapCard aCard = new BootstrapCard ();\n" +
                                                                             "aCard.createAndAddHeader ().addChild (\"Header\");\n" +
                                                                             "aCard.createAndAddBody ().addChild (\"Body\");"));
  }

  @Override
  protected void fillContent (@NonNull final WebPageExecutionContext aWPEC)
  {
    final HCNodeList aNodeList = aWPEC.getNodeList ();

    aNodeList.addChild (new HCP ().addChild ("This page contains all controls that have no demo page of their own. ")
                                  .addChild (new HCSpan ().addChild ("Use it to check the controls that can only be verified in a browser."))
                                  .addChild (" See also ")
                                  .addChild (new HCA (new SimpleURL ("https://getbootstrap.com/docs/5.3/")).addChild ("the Bootstrap 5 documentation"))
                                  .addChild ("."));

    _addJSComponents (aNodeList);
    _addFormControls (aNodeList);
    _addContentComponents (aNodeList);
    _addUICtrls (aNodeList, aWPEC.getDisplayLocale ());
  }
}
