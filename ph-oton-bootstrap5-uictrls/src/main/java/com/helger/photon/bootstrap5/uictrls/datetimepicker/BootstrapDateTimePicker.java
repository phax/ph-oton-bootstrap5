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
package com.helger.photon.bootstrap5.uictrls.datetimepicker;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Locale;
import java.util.Map;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import com.helger.base.enforce.ValueEnforcer;
import com.helger.base.state.ETriState;
import com.helger.base.string.StringHelper;
import com.helger.collection.commons.CommonsLinkedHashMap;
import com.helger.collection.commons.ICommonsOrderedMap;
import com.helger.datetime.helper.PDTFactory;
import com.helger.html.css.DefaultCSSClassProvider;
import com.helger.html.css.ICSSClassProvider;
import com.helger.html.hc.IHCConversionSettingsToNode;
import com.helger.html.hc.IHCHasChildrenMutable;
import com.helger.html.hc.IHCNode;
import com.helger.html.hc.html.forms.HCEdit;
import com.helger.html.hc.html.textlevel.HCSpan;
import com.helger.html.jscode.IJSExpression;
import com.helger.html.jscode.JSAssocArray;
import com.helger.html.jscode.JSExpr;
import com.helger.html.jscode.JSInvocation;
import com.helger.html.jscode.JSVar;
import com.helger.html.jscode.html.JSHtml;
import com.helger.photon.app.html.PhotonCSS;
import com.helger.photon.app.html.PhotonJS;
import com.helger.photon.bootstrap5.CBootstrapCSS;
import com.helger.photon.bootstrap5.inputgroup.BootstrapInputGroup;
import com.helger.photon.bootstrap5.uictrls.EBootstrapUICtrlsCSSPathProvider;
import com.helger.photon.bootstrap5.uictrls.EBootstrapUICtrlsJSPathProvider;
import com.helger.photon.core.form.RequestField;
import com.helger.photon.icon.fontawesome.EFontAwesome4Icon;

/**
 * This class represents a wrapper around the DateTime Picker for Bootstrap 5 from
 * https://github.com/Eonasdan/tempus-dominus<br>
 *
 * @author Philip Helger
 */
public class BootstrapDateTimePicker extends BootstrapInputGroup
{
  public static final ICSSClassProvider CSS_CLASS_DATE = DefaultCSSClassProvider.create ("date");
  public static final ICSSClassProvider CSS_CLASS_DATETIMEPICKER_INPUT = DefaultCSSClassProvider.create ("datetimepicker-input");

  public static final String EVENT_SUFFIX = ".datetimepicker";
  public static final String EVENT_NAME_CHANGE = "change" + EVENT_SUFFIX;

  public static final EBootstrap5DateTimePickerViewModeType DEFAULT_VIEW_MODE = EBootstrap5DateTimePickerViewModeType.CALENDAR;

  // Use the calendar icon as default prefix
  public static final IHCNode DEFAULT_PREPEND_ICON = EFontAwesome4Icon.CALENDAR.getAsNode ();

  private final HCEdit m_aEdit;
  private final Locale m_aDisplayLocale;
  private LocalDateTime m_aInitialDate;
  private ETriState m_eShowCalendarWeeks = ETriState.FALSE;
  private ETriState m_eShowToday = ETriState.TRUE;
  private ETriState m_eShowClear = ETriState.TRUE;
  private ETriState m_eShowClose = ETriState.TRUE;
  private EBootstrap5DateTimePickerMode m_eMode = EBootstrap5DateTimePickerMode.DEFAULT;
  private String m_sFormat;
  private EBootstrap5DateTimePickerViewModeType m_eViewMode;
  private ETriState m_eSideBySide = ETriState.UNDEFINED;
  private LocalDateTime m_aMinDate;
  private LocalDateTime m_aMaxDate;
  private ETriState m_eUseCurrent = ETriState.FALSE;
  private IHCNode m_aPrependIcon = DEFAULT_PREPEND_ICON;
  private final ICommonsOrderedMap <String, String> m_aIcons = new CommonsLinkedHashMap <> ();

  public BootstrapDateTimePicker (@NonNull final HCEdit aEdit, @NonNull final Locale aDisplayLocale)
  {
    ValueEnforcer.notNull (aEdit, "Edit");
    ValueEnforcer.notNull (aDisplayLocale, "DisplayLocale");
    m_aEdit = aEdit;
    m_aDisplayLocale = aDisplayLocale;

    // Default icons for FontAwesome 4
    m_aIcons.put ("time", "fa fa-clock-o");
    m_aIcons.put ("date", "fa fa-calendar");
    m_aIcons.put ("up", "fa fa-arrow-up");
    m_aIcons.put ("down", "fa fa-arrow-down");
    m_aIcons.put ("previous", "fa fa-chevron-left");
    m_aIcons.put ("next", "fa fa-chevron-right");
    m_aIcons.put ("today", "fa fa-calendar-check-o");
    m_aIcons.put ("clear", "fa fa-trash");
    m_aIcons.put ("close", "fa fa-times");

    ensureID ();
    // Ensure the edit has an ID
    m_aEdit.ensureID ();
    // Add the specific class to the edit
    m_aEdit.addClass (CSS_CLASS_DATETIMEPICKER_INPUT);
    // Data attribute for TD6 to find the target
    m_aEdit.customAttrs ().setDataAttr ("td-target", "#" + getID ());

    // Add the specific class to the group
    addClass (CSS_CLASS_DATE);
    // Data attribute for TD6
    customAttrs ().setDataAttr ("td-target-input", "nearest");
    customAttrs ().setDataAttr ("td-target-toggle", "nearest");
  }

  @NonNull
  public HCEdit getEdit ()
  {
    return m_aEdit;
  }

  @NonNull
  public BootstrapDateTimePicker setInitialDate (@Nullable final LocalDate aInitialDate)
  {
    return setInitialDate (aInitialDate == null ? null : LocalDateTime.of (aInitialDate, LocalTime.MIDNIGHT));
  }

  @NonNull
  public BootstrapDateTimePicker setInitialDate (@Nullable final LocalTime aInitialTime)
  {
    return setInitialDate (aInitialTime == null ? null : LocalDateTime.of (PDTFactory.getCurrentLocalDate (),
                                                                           aInitialTime));
  }

  @NonNull
  public BootstrapDateTimePicker setInitialDate (@Nullable final LocalDateTime aInitialDate)
  {
    m_aInitialDate = aInitialDate;
    return this;
  }

  @NonNull
  public BootstrapDateTimePicker setShowCalendarWeeks (final boolean bShowCalendarWeeks)
  {
    m_eShowCalendarWeeks = ETriState.valueOf (bShowCalendarWeeks);
    return this;
  }

  @NonNull
  public BootstrapDateTimePicker setShowToday (final boolean bShowToday)
  {
    m_eShowToday = ETriState.valueOf (bShowToday);
    return this;
  }

  @NonNull
  public BootstrapDateTimePicker setShowClear (final boolean bShowClear)
  {
    m_eShowClear = ETriState.valueOf (bShowClear);
    return this;
  }

  @NonNull
  public BootstrapDateTimePicker setShowClose (final boolean bShowClose)
  {
    m_eShowClose = ETriState.valueOf (bShowClose);
    return this;
  }

  @NonNull
  public BootstrapDateTimePicker setMode (@NonNull final EBootstrap5DateTimePickerMode eMode)
  {
    ValueEnforcer.notNull (eMode, "Mode");
    m_eMode = eMode;
    return this;
  }

  @NonNull
  public BootstrapDateTimePicker setFormat (@Nullable final String sFormat)
  {
    m_sFormat = sFormat;
    return this;
  }

  @NonNull
  public BootstrapDateTimePicker setViewMode (@Nullable final EBootstrap5DateTimePickerViewModeType eViewMode)
  {
    m_eViewMode = eViewMode;
    return this;
  }

  @NonNull
  public BootstrapDateTimePicker setSideBySide (final boolean bSideBySide)
  {
    m_eSideBySide = ETriState.valueOf (bSideBySide);
    return this;
  }

  @NonNull
  public BootstrapDateTimePicker setMinDate (@Nullable final LocalDate aMinDate)
  {
    return setMinDate (aMinDate == null ? null : LocalDateTime.of (aMinDate, LocalTime.MIDNIGHT));
  }

  @NonNull
  public BootstrapDateTimePicker setMinDate (@Nullable final LocalDateTime aMinDate)
  {
    m_aMinDate = aMinDate;
    return this;
  }

  @NonNull
  public BootstrapDateTimePicker setMaxDate (@Nullable final LocalDate aMaxDate)
  {
    return setMaxDate (aMaxDate == null ? null : LocalDateTime.of (aMaxDate, LocalTime.MIDNIGHT));
  }

  @NonNull
  public BootstrapDateTimePicker setMaxDate (@Nullable final LocalDateTime aMaxDate)
  {
    m_aMaxDate = aMaxDate;
    return this;
  }

  @NonNull
  public BootstrapDateTimePicker setUseCurrent (final boolean bUseCurrent)
  {
    m_eUseCurrent = ETriState.valueOf (bUseCurrent);
    return this;
  }

  @NonNull
  public BootstrapDateTimePicker setPrependIcon (@Nullable final IHCNode aPrependIcon)
  {
    m_aPrependIcon = aPrependIcon;
    return this;
  }

  @NonNull
  public JSAssocArray getJSOptions ()
  {
    final JSAssocArray aOptions = new JSAssocArray ();

    // 1. Display Options
    final JSAssocArray aDisplay = new JSAssocArray ();

    // Side By Side
    if (m_eSideBySide.isDefined ())
      aDisplay.add ("sideBySide", m_eSideBySide.getAsBooleanValue (false));

    // Calendar weeks?
    if (m_eShowCalendarWeeks.isDefined ())
      aDisplay.add ("calendarWeeks", m_eShowCalendarWeeks.getAsBooleanValue (false));

    // View Mode
    if (m_eViewMode != null)
      aDisplay.add ("viewMode", m_eViewMode.getJSValueString ());

    // Components (Calendar/Clock)
    final JSAssocArray aComponents = new JSAssocArray ();
    aComponents.add ("calendar", m_eMode.isDateContained ());
    aComponents.add ("clock", m_eMode.isTimeContained ());
    aDisplay.add ("components", aComponents);

    // Buttons
    final JSAssocArray aButtons = new JSAssocArray ();
    if (m_eShowToday.isDefined ())
      aButtons.add ("today", m_eShowToday.getAsBooleanValue (true));
    if (m_eShowClear.isDefined ())
      aButtons.add ("clear", m_eShowClear.getAsBooleanValue (true));
    if (m_eShowClose.isDefined ())
      aButtons.add ("close", m_eShowClose.getAsBooleanValue (true));
    aDisplay.add ("buttons", aButtons);

    // Icons
    if (m_aIcons.isNotEmpty ())
    {
      final JSAssocArray aIcons = new JSAssocArray ();
      for (final Map.Entry <String, String> aEntry : m_aIcons.entrySet ())
        aIcons.add (aEntry.getKey (), aEntry.getValue ());
      aDisplay.add ("icons", aIcons);
    }

    aOptions.add ("display", aDisplay);

    // 2. Localization Options
    final JSAssocArray aLocalization = new JSAssocArray ();
    // Texts
    aLocalization.add ("today", EBootstrap5DateTimePickerTexts.TODAY.getDisplayText (m_aDisplayLocale));
    aLocalization.add ("clear", EBootstrap5DateTimePickerTexts.CLEAR.getDisplayText (m_aDisplayLocale));
    aLocalization.add ("close", EBootstrap5DateTimePickerTexts.CLOSE.getDisplayText (m_aDisplayLocale));
    aLocalization.add ("selectMonth", EBootstrap5DateTimePickerTexts.SELECT_MONTH.getDisplayText (m_aDisplayLocale));
    aLocalization.add ("previousMonth", EBootstrap5DateTimePickerTexts.PREV_MONTH.getDisplayText (m_aDisplayLocale));
    aLocalization.add ("nextMonth", EBootstrap5DateTimePickerTexts.NEXT_MONTH.getDisplayText (m_aDisplayLocale));
    aLocalization.add ("selectYear", EBootstrap5DateTimePickerTexts.SELECT_YEAR.getDisplayText (m_aDisplayLocale));
    aLocalization.add ("previousYear", EBootstrap5DateTimePickerTexts.PREV_YEAR.getDisplayText (m_aDisplayLocale));
    aLocalization.add ("nextYear", EBootstrap5DateTimePickerTexts.NEXT_YEAR.getDisplayText (m_aDisplayLocale));
    aLocalization.add ("selectDecade", EBootstrap5DateTimePickerTexts.SELECT_DECADE.getDisplayText (m_aDisplayLocale));
    aLocalization.add ("previousDecade", EBootstrap5DateTimePickerTexts.PREV_DECADE.getDisplayText (m_aDisplayLocale));
    aLocalization.add ("nextDecade", EBootstrap5DateTimePickerTexts.NEXT_DECADE.getDisplayText (m_aDisplayLocale));
    aLocalization.add ("previousCentury",
                       EBootstrap5DateTimePickerTexts.PREV_CENTURY.getDisplayText (m_aDisplayLocale));
    aLocalization.add ("nextCentury", EBootstrap5DateTimePickerTexts.NEXT_CENTURY.getDisplayText (m_aDisplayLocale));
    aLocalization.add ("pickHour", EBootstrap5DateTimePickerTexts.PICK_HOUR.getDisplayText (m_aDisplayLocale));
    aLocalization.add ("incrementHour",
                       EBootstrap5DateTimePickerTexts.INCREMENT_HOUR.getDisplayText (m_aDisplayLocale));
    aLocalization.add ("decrementHour",
                       EBootstrap5DateTimePickerTexts.DECREMENT_HOUR.getDisplayText (m_aDisplayLocale));
    aLocalization.add ("pickMinute", EBootstrap5DateTimePickerTexts.PICK_MINUTE.getDisplayText (m_aDisplayLocale));
    aLocalization.add ("incrementMinute",
                       EBootstrap5DateTimePickerTexts.INCREMENT_MINUTE.getDisplayText (m_aDisplayLocale));
    aLocalization.add ("decrementMinute",
                       EBootstrap5DateTimePickerTexts.DECREMENT_MINUTE.getDisplayText (m_aDisplayLocale));
    aLocalization.add ("pickSecond", EBootstrap5DateTimePickerTexts.PICK_SECOND.getDisplayText (m_aDisplayLocale));
    aLocalization.add ("incrementSecond",
                       EBootstrap5DateTimePickerTexts.INCREMENT_SECOND.getDisplayText (m_aDisplayLocale));
    aLocalization.add ("decrementSecond",
                       EBootstrap5DateTimePickerTexts.DECREMENT_SECOND.getDisplayText (m_aDisplayLocale));
    aLocalization.add ("toggleMeridiem",
                       EBootstrap5DateTimePickerTexts.TOGGLE_MERIDIEM.getDisplayText (m_aDisplayLocale));
    aLocalization.add ("selectTime", EBootstrap5DateTimePickerTexts.SELECT_TIME.getDisplayText (m_aDisplayLocale));
    aLocalization.add ("selectDate", EBootstrap5DateTimePickerTexts.SELECT_DATE.getDisplayText (m_aDisplayLocale));

    // Locale
    aLocalization.add ("locale", m_aDisplayLocale.toLanguageTag ());
    // Default to 24h for now, can be improved
    aLocalization.add ("hourCycle", "h23");

    // Format
    String sJSFormat = m_sFormat;
    if (StringHelper.isEmpty (sJSFormat))
      sJSFormat = m_eMode.getJSFormat (m_aDisplayLocale);
    aLocalization.add ("format", sJSFormat);

    aOptions.add ("localization", aLocalization);

    // 3. Restrictions Options
    final JSAssocArray aRestrictions = new JSAssocArray ();
    if (m_aMinDate != null)
      aRestrictions.add ("minDate", m_aMinDate.toString ());
    if (m_aMaxDate != null)
      aRestrictions.add ("maxDate", m_aMaxDate.toString ());

    if (aRestrictions.isNotEmpty ())
      aOptions.add ("restrictions", aRestrictions);

    // 4. Other Options
    if (m_eUseCurrent.isDefined ())
      aOptions.add ("useCurrent", m_eUseCurrent.getAsBooleanValue (false));

    // Initial Date (defaultDate in v6 is handled via defaultDate option or setting value)
    if (m_aInitialDate != null)
      aOptions.add ("defaultDate", m_aInitialDate.toString ());

    return aOptions;
  }

  @NonNull
  public JSVar getJSInitCode ()
  {
    // new tempusDominus.TempusDominus(document.getElementById('id'), options);
    return new JSVar ("td" + getID (), invoke ());
  }

  @NonNull
  public static JSInvocation invoke (@NonNull final IJSExpression aParentElement, @NonNull final JSAssocArray aOptions)
  {
    return JSExpr.ref ("tempusDominus").invoke ("TempusDominus").arg (aParentElement).arg (aOptions);
  }

  @NonNull
  public final JSInvocation invoke ()
  {
    return invoke (JSHtml.documentGetElementById (getID ()), getJSOptions ());
  }

  @Override
  protected void onFinalizeNodeState (@NonNull final IHCConversionSettingsToNode aConversionSettings,
                                      @NonNull final IHCHasChildrenMutable <?, ? super IHCNode> aTargetNode)
  {
    super.onFinalizeNodeState (aConversionSettings, aTargetNode);

    // Add the edit
    addChild (m_aEdit);

    // Add the toggle button
    final HCSpan aToggle = new HCSpan ().addClass (CBootstrapCSS.INPUT_GROUP_TEXT);
    aToggle.customAttrs ().setDataAttr ("td-target", "#" + getID ());
    aToggle.customAttrs ().setDataAttr ("td-toggle", "datetimepicker");
    if (m_aPrependIcon != null)
      aToggle.addChild (m_aPrependIcon);

    // Wrap in input group append/prepend if needed, but for now just add it
    // Bootstrap 5 Input Group structure:
    // <div class="input-group" id="datetimepicker1" data-td-target-input="nearest"
    // data-td-target-toggle="nearest">
    // <input id="datetimepicker1Input" type="text" class="form-control"
    // data-td-target="#datetimepicker1">
    // <span class="input-group-text" data-td-target="#datetimepicker1"
    // data-td-toggle="datetimepicker">
    // <i class="fas fa-calendar"></i>
    // </span>
    // </div>

    // We are inheriting from BootstrapInputGroup, so we should use its methods
    // But BootstrapInputGroup in BS5 might be different.
    // Assuming we can just add the toggle as an appended element.
    addChild (aToggle);

    // JS Code
    // We don't add inline script here usually, it's done via Bootstrap5DateTimePickerJS
    // But if we wanted to:
    // aTargetNode.addChild (new HCScriptInline (getJSInitCode ()));
  }

  @Override
  protected void onRegisterExternalResources (@NonNull final IHCConversionSettingsToNode aConversionSettings,
                                              final boolean bForceRegistration)
  {
    PhotonJS.registerJSIncludeForThisRequest (EBootstrapUICtrlsJSPathProvider.DATETIMEPICKER);
    PhotonCSS.registerCSSIncludeForThisRequest (EBootstrapUICtrlsCSSPathProvider.DATETIMEPICKER);
  }

  @NonNull
  public static BootstrapDateTimePicker create (@NonNull final String sName,
                                                @Nullable final LocalDate aInitialValue,
                                                @NonNull final Locale aDisplayLocale)
  {
    return new BootstrapDateTimePicker (new HCEdit (new RequestField (sName)), aDisplayLocale).setInitialDate (
                                                                                                               aInitialValue)
                                                                                              .setMode (EBootstrap5DateTimePickerMode.DATE);
  }

  @NonNull
  public static BootstrapDateTimePicker create (@NonNull final String sName,
                                                @Nullable final LocalTime aInitialValue,
                                                @NonNull final Locale aDisplayLocale)
  {
    return new BootstrapDateTimePicker (new HCEdit (new RequestField (sName)), aDisplayLocale).setInitialDate (
                                                                                                               aInitialValue)
                                                                                              .setMode (EBootstrap5DateTimePickerMode.TIME);
  }

  @NonNull
  public static BootstrapDateTimePicker create (@NonNull final String sName,
                                                @Nullable final LocalDateTime aInitialValue,
                                                @NonNull final Locale aDisplayLocale)
  {
    return new BootstrapDateTimePicker (new HCEdit (new RequestField (sName)), aDisplayLocale).setInitialDate (
                                                                                                               aInitialValue)
                                                                                              .setMode (EBootstrap5DateTimePickerMode.DATE_TIME);
  }

  @NonNull
  public static BootstrapDateTimePicker create (@NonNull final String sName,
                                                @NonNull final Locale aDisplayLocale,
                                                @NonNull final EBootstrap5DateTimePickerMode eMode)
  {
    return new BootstrapDateTimePicker (new HCEdit (new RequestField (sName)), aDisplayLocale).setMode (eMode);
  }
}
