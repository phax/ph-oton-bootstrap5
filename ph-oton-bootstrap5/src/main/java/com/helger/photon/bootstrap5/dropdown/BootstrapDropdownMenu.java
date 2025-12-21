/*
 * Copyright (C) 2018-2025 Philip Helger (www.helger.com)
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
package com.helger.photon.bootstrap5.dropdown;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import com.helger.html.EHTMLElement;
import com.helger.html.EHTMLRole;
import com.helger.html.hc.IHCConversionSettingsToNode;
import com.helger.html.hc.IHCHasChildrenMutable;
import com.helger.html.hc.IHCNode;
import com.helger.html.hc.html.IHCElementWithChildren;
import com.helger.html.hc.html.forms.HCButton;
import com.helger.html.hc.html.grouping.AbstractHCUL;
import com.helger.html.hc.html.textlevel.HCSpan;
import com.helger.photon.bootstrap5.CBootstrapCSS;
import com.helger.photon.bootstrap5.buttongroup.BootstrapButtonGroup;

/**
 * Bootstrap dropdown menu, without integration into surrounding objects.
 *
 * @author Philip Helger
 */
public class BootstrapDropdownMenu extends AbstractHCUL <BootstrapDropdownMenu>
{
  /**
   * This event fires immediately when the show instance method is called.
   */
  public static final String JS_EVENT_SHOW = "show.bs.dropdown";
  /**
   * This event is fired when the dropdown has been made visible to the user (will wait for CSS
   * transitions, to complete).
   */
  public static final String JS_EVENT_SHOWN = "shown.bs.dropdown";
  /**
   * This event is fired immediately when the hide instance method has been called.
   */
  public static final String JS_EVENT_HIDE = "hide.bs.dropdown";
  /**
   * This event is fired when the dropdown has finished being hidden from the user (will wait for
   * CSS transitions, to complete).
   */
  public static final String JS_EVENT_HIDDEN = "hidden.bs.dropdown";

  // Means right-align in LTR texts
  public static final boolean DEFAULT_ALIGN_END = false;

  private boolean m_bAlignEnd = DEFAULT_ALIGN_END;

  public BootstrapDropdownMenu ()
  {}

  public final boolean isAlignEnd ()
  {
    return m_bAlignEnd;
  }

  @NonNull
  public final BootstrapDropdownMenu setAlignEnd (final boolean bAlignEnd)
  {
    m_bAlignEnd = bAlignEnd;
    return this;
  }

  @NonNull
  public BootstrapDropdownItem createAndAddItem ()
  {
    final BootstrapDropdownItem ret = new BootstrapDropdownItem ();
    addItem (ret);
    return ret;
  }

  @NonNull
  public BootstrapDropdownDivider createAndAddDivider ()
  {
    final BootstrapDropdownDivider ret = new BootstrapDropdownDivider ();
    addItem (ret);
    return ret;
  }

  @NonNull
  public BootstrapDropdownText createAndAddText ()
  {
    return createAndAddText (null);
  }

  @NonNull
  public BootstrapDropdownText createAndAddText (@Nullable final String sText)
  {
    final BootstrapDropdownText aText = new BootstrapDropdownText ();
    aText.addChild (sText);
    addItem (aText);
    return aText;
  }

  @NonNull
  public BootstrapDropdownHeader createAndAddHeader ()
  {
    final BootstrapDropdownHeader ret = new BootstrapDropdownHeader ();
    addItem (ret);
    return ret;
  }

  @Override
  protected void onFinalizeNodeState (@NonNull final IHCConversionSettingsToNode aConversionSettings,
                                      @NonNull final IHCHasChildrenMutable <?, ? super IHCNode> aTargetNode)
  {
    super.onFinalizeNodeState (aConversionSettings, aTargetNode);
    addClass (CBootstrapCSS.DROPDOWN_MENU);
    if (m_bAlignEnd)
      addClass (CBootstrapCSS.DROPDOWN_MENU_END);
  }

  public static void assignMenuToButton (@NonNull final IHCElementWithChildren <?> aContainer,
                                         @NonNull final IHCElementWithChildren <?> aButton,
                                         @NonNull final BootstrapDropdownMenu aMenu,
                                         @NonNull final EBootstrapDropType eDropType,
                                         final boolean bUseSplitButton)
  {
    // Container div
    IHCElementWithChildren <?> aDropContainer = aContainer;

    // Ensure it is a button - hopefully redundant
    aButton.addClass (CBootstrapCSS.BTN);

    IHCElementWithChildren <?> aActionButton;
    if (bUseSplitButton)
    {
      final HCButton aSplitButton = new HCButton ();
      aSplitButton.addClasses (aButton.getAllClasses ());
      aSplitButton.addClass (CBootstrapCSS.DROPDOWN_TOGGLE_SPLIT);
      aSplitButton.addChild (new HCSpan ().addClass (CBootstrapCSS.VISUALLY_HIDDEN)
                                          .addChild ("Toggle " + eDropType.name ()));

      if (eDropType == EBootstrapDropType.DROPSTART)
      {
        final BootstrapButtonGroup aTmpGroup = new BootstrapButtonGroup ();
        aTmpGroup.addChild (aSplitButton);
        aTmpGroup.addChild (aMenu);
        aContainer.addChild (aTmpGroup);
        aContainer.addChild (aButton);
        aDropContainer = aTmpGroup;
      }
      else
      {
        aContainer.addChild (aButton);
        aContainer.addChild (aSplitButton);
        aContainer.addChild (aMenu);
      }

      aActionButton = aSplitButton;
    }
    else
    {
      aContainer.addChild (aButton);
      aContainer.addChild (aMenu);

      aActionButton = aButton;
    }

    aActionButton.addClass (CBootstrapCSS.DROPDOWN_TOGGLE);
    if (aActionButton.getElement () != EHTMLElement.BUTTON)
      aActionButton.setRole (EHTMLRole.BUTTON);
    aActionButton.ensureID ();
    aActionButton.customAttrs ().setDataAttr ("bs-toggle", "dropdown");
    aActionButton.customAttrs ().setAriaExpanded (false);

    // Labeled by original button!
    aMenu.customAttrs ().setAriaLabeledBy (aButton);

    // Container (dropdown, -start, -end, -up)
    aDropContainer.addClass (eDropType);
  }
}
