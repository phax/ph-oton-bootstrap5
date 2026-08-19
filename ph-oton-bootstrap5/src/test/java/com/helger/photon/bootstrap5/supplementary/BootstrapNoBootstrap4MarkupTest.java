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
package com.helger.photon.bootstrap5.supplementary;

import static com.helger.photon.bootstrap5.mock.BootstrapTestHelper.getAsHTMLString;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

import com.helger.html.hc.html.forms.HCEdit;
import com.helger.html.hc.impl.HCNodeList;
import com.helger.photon.bootstrap5.alert.BootstrapSuccessBox;
import com.helger.photon.bootstrap5.badge.BootstrapBadge;
import com.helger.photon.bootstrap5.badge.EBootstrapBadgeType;
import com.helger.photon.bootstrap5.breadcrumb.BootstrapBreadcrumb;
import com.helger.photon.bootstrap5.button.BootstrapButton;
import com.helger.photon.bootstrap5.card.BootstrapCard;
import com.helger.photon.bootstrap5.dropdown.BootstrapDropdownMenu;
import com.helger.photon.bootstrap5.form.BootstrapFormFloating;
import com.helger.photon.bootstrap5.grid.BootstrapGridSpec;
import com.helger.photon.bootstrap5.grid.BootstrapRow;
import com.helger.photon.bootstrap5.inputgroup.BootstrapInputGroup;
import com.helger.photon.bootstrap5.layout.BootstrapContainer;
import com.helger.photon.bootstrap5.listgroup.BootstrapListGroup;
import com.helger.photon.bootstrap5.modal.BootstrapModal;
import com.helger.photon.bootstrap5.nav.BootstrapNav;
import com.helger.photon.bootstrap5.nav.EBootstrapNavType;
import com.helger.photon.bootstrap5.navbar.BootstrapNavbar;
import com.helger.photon.bootstrap5.offcanvas.BootstrapOffcanvas;
import com.helger.photon.bootstrap5.table.BootstrapTable;
import com.helger.photon.bootstrap5.utils.BootstrapCloseIcon;

/**
 * Test that no Bootstrap 3 or Bootstrap 4 only markup is emitted by the components.
 *
 * @author Philip Helger
 */
public final class BootstrapNoBootstrap4MarkupTest
{
  /** Class names and attributes that no longer exist in Bootstrap 5 */
  private static final String [] OUTDATED_MARKUP = { "input-group-prepend",
                                                     "input-group-append",
                                                     "jumbotron",
                                                     "form-row",
                                                     "sr-only",
                                                     "custom-control",
                                                     "custom-select",
                                                     "custom-file",
                                                     "badge-pill",
                                                     "badge-danger",
                                                     "no-gutters",
                                                     "media-body",
                                                     "font-weight-",
                                                     "text-left",
                                                     "text-right",
                                                     "float-left",
                                                     "float-right",
                                                     "dropdown-menu-right",
                                                     "table-condensed",
                                                     "class=\"close\"",
                                                     "data-toggle=",
                                                     "data-target=",
                                                     "data-dismiss=",
                                                     "data-ride=",
                                                     "data-parent=" };

  private static HCNodeList _createAllComponents ()
  {
    final HCNodeList aNL = new HCNodeList ();

    final BootstrapContainer aContainer = aNL.addAndReturnChild (new BootstrapContainer ());

    final BootstrapRow aRow = aContainer.addAndReturnChild (new BootstrapRow ());
    aRow.createColumn (BootstrapGridSpec.create (12, 6, 4, 3, 2, 1)).addChild ("Column");

    final BootstrapCard aCard = aContainer.addAndReturnChild (new BootstrapCard ());
    aCard.createAndAddHeader ().addChild ("Header");
    aCard.createAndAddBody ().addChild (new BootstrapBadge (EBootstrapBadgeType.DANGER).setPill (true).addChild ("1"));
    aCard.createAndAddFooter ().addChild (new BootstrapButton ().addChild ("Button"));

    aContainer.addChild (new BootstrapSuccessBox ().setShowClose (true).addChild ("Done"));
    aContainer.addChild (new BootstrapCloseIcon ());

    final BootstrapInputGroup aIG = aContainer.addAndReturnChild (new BootstrapInputGroup ());
    aIG.addChildPrefix ("@").addChild (new HCEdit ("field")).addChildSuffix (".com");

    aContainer.addChild (new BootstrapFormFloating (new HCEdit ("floating"), "Label"));

    final BootstrapNavbar aNavbar = aContainer.addAndReturnChild (new BootstrapNavbar ());
    aNavbar.addToggler ("target1");

    final BootstrapOffcanvas aOffcanvas = aContainer.addAndReturnChild (new BootstrapOffcanvas ());
    aOffcanvas.setHeader ("Menu").setBody ("Content");
    aOffcanvas.applyToggleTo (aContainer.addAndReturnChild (new BootstrapButton ().addChild ("Open")));

    final BootstrapBreadcrumb aBC = aContainer.addAndReturnChild (new BootstrapBreadcrumb ());
    aBC.getList ().addActive ("Here");

    aContainer.addChild (new BootstrapListGroup ().addItem ("Item"));

    final BootstrapDropdownMenu aDD = aContainer.addAndReturnChild (new BootstrapDropdownMenu ());
    aDD.setAlignEnd (true);
    aDD.createAndAddItem ().addChild ("Item");
    aDD.createAndAddDivider ();

    aContainer.addChild (new BootstrapNav (EBootstrapNavType.TABS));

    final BootstrapTable aTable = aContainer.addAndReturnChild (new BootstrapTable ());
    aTable.setStriped (true).setCondensed (true);
    aTable.addBodyRow ().addCells ("a", "b");

    aContainer.addChild (new BootstrapModal ());

    return aNL;
  }

  @Test
  public void testNoOutdatedMarkup ()
  {
    final String sHTML = getAsHTMLString (_createAllComponents ());
    for (final String sOutdated : OUTDATED_MARKUP)
      assertFalse ("Rendered HTML contains the outdated markup '" + sOutdated + "': " + sHTML,
                   sHTML.contains (sOutdated));
  }
}
