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
import java.time.Month;
import java.util.Locale;
import java.util.Map;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import com.helger.annotation.style.ReturnsMutableObject;
import com.helger.base.enforce.ValueEnforcer;
import com.helger.base.state.ETriState;
import com.helger.base.string.StringHelper;
import com.helger.collection.commons.CommonsLinkedHashMap;
import com.helger.collection.commons.ICommonsOrderedMap;
import com.helger.datetime.format.PDTToString;
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
import com.helger.html.jscode.html.JSHtml;
import com.helger.html.jscode.type.JSPrimitiveTypes;
import com.helger.photon.app.html.PhotonCSS;
import com.helger.photon.app.html.PhotonJS;
import com.helger.photon.bootstrap5.CBootstrapCSS;
import com.helger.photon.bootstrap5.form.BootstrapFormHelper;
import com.helger.photon.bootstrap5.inputgroup.BootstrapInputGroup;
import com.helger.photon.bootstrap5.uictrls.EBootstrapUICtrlsCSSPathProvider;
import com.helger.photon.bootstrap5.uictrls.EBootstrapUICtrlsJSPathProvider;
import com.helger.photon.core.form.RequestField;
import com.helger.photon.icon.fontawesome5.CFontAwesome5CSS;
import com.helger.photon.icon.fontawesome5.EFontAwesome5Icon;

/**
 * This class represents a wrapper around the DateTime Picker for Bootstrap 5 (Tempus Dominus v6)
 * from https://github.com/Eonasdan/tempus-dominus<br>
 *
 * @author Philip Helger
 */
public class BootstrapDateTimePicker extends BootstrapInputGroup
{
  public static final ICSSClassProvider CSS_CLASS_DATE = DefaultCSSClassProvider.create ("date");
  public static final ICSSClassProvider CSS_CLASS_DATETIMEPICKER_INPUT = DefaultCSSClassProvider.create ("datetimepicker-input");

  /** Tempus Dominus v6 event namespace suffix */
  public static final String EVENT_SUFFIX = ".td";
  public static final String EVENT_NAME_CHANGE = "change" + EVENT_SUFFIX;

  public static final EBootstrap5DateTimePickerViewModeType DEFAULT_VIEW_MODE = EBootstrap5DateTimePickerViewModeType.CALENDAR;

  // Use the calendar icon as default prefix
  public static final IHCNode DEFAULT_PREPEND_ICON = EFontAwesome5Icon.CALENDAR.getAsNode ();

  private static final LocalDate DUMMY_DATE = PDTFactory.createLocalDate (2018, Month.OCTOBER, 24);
  private static final LocalTime DUMMY_TIME = PDTFactory.createLocalTime (12, 10, 34);

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

  @NonNull
  private static String _fa (@NonNull final ICSSClassProvider aIcon)
  {
    return CFontAwesome5CSS.FA.getCSSClass () + " " + aIcon.getCSSClass ();
  }

  @Nullable
  public static String getAsModeSpecificUIString (@NonNull final EBootstrap5DateTimePickerMode eMode,
                                                  @Nullable final LocalDateTime aDT,
                                                  @NonNull final Locale aDisplayLocale)
  {
    if (aDT == null)
      return null;
    return switch (eMode)
    {
      case TIME -> PDTToString.getAsString (aDT.toLocalTime (), aDisplayLocale);
      case DATE -> PDTToString.getAsString (aDT.toLocalDate (), aDisplayLocale);
      case DATE_TIME -> PDTToString.getAsString (aDT, aDisplayLocale);
      default -> throw new IllegalStateException ("Unsupported mode " + eMode);
    };
  }

  /**
   * Constructor.
   *
   * @param sName
   *        Field name. May not be <code>null</code>.
   * @param aInitialValue
   *        Field initial value. May be <code>null</code>.
   * @param aDisplayLocale
   *        The locale to use. May not be <code>null</code>.
   * @param eMode
   *        Mode to use. May not be <code>null</code>.
   */
  protected BootstrapDateTimePicker (@NonNull final String sName,
                                     @Nullable final LocalDateTime aInitialValue,
                                     @NonNull final Locale aDisplayLocale,
                                     @NonNull final EBootstrap5DateTimePickerMode eMode)
  {
    ValueEnforcer.notNull (aDisplayLocale, "DisplayLocale");
    ValueEnforcer.notNull (eMode, "Mode");

    m_aDisplayLocale = aDisplayLocale;
    m_aInitialDate = aInitialValue;

    // Customize UI
    ensureID ();
    customAttrs ().setDataAttr ("td-target-input", "nearest");

    m_aEdit = new HCEdit (new RequestField (sName, getAsModeSpecificUIString (eMode, aInitialValue, aDisplayLocale)));
    m_aEdit.setPlaceholder ("");
    m_aEdit.addClass (CSS_CLASS_DATETIMEPICKER_INPUT);
    m_aEdit.customAttrs ().setDataAttr ("td-target", "#" + getID ());
    BootstrapFormHelper.markAsFormControl (m_aEdit);

    addChild (m_aEdit);

    setMode (eMode);

    // Explicit icons, as the Tempus Dominus defaults require FontAwesome 6
    m_aIcons.put ("time", _fa (CFontAwesome5CSS.FA_CLOCK));
    m_aIcons.put ("date", _fa (CFontAwesome5CSS.FA_CALENDAR));
    m_aIcons.put ("up", _fa (CFontAwesome5CSS.FA_ARROW_UP));
    m_aIcons.put ("down", _fa (CFontAwesome5CSS.FA_ARROW_DOWN));
    m_aIcons.put ("previous", _fa (CFontAwesome5CSS.FA_CHEVRON_LEFT));
    m_aIcons.put ("next", _fa (CFontAwesome5CSS.FA_CHEVRON_RIGHT));
    m_aIcons.put ("today", _fa (CFontAwesome5CSS.FA_CALENDAR_CHECK));
    m_aIcons.put ("clear", _fa (CFontAwesome5CSS.FA_ERASER));
    m_aIcons.put ("close", _fa (CFontAwesome5CSS.FA_TIMES));
  }

  /**
   * @return The contained edit. You may modify the styles.
   */
  @NonNull
  public final HCEdit getEdit ()
  {
    return m_aEdit;
  }

  @NonNull
  public final BootstrapDateTimePicker setReadOnly (final boolean bReadOnly)
  {
    m_aEdit.setReadOnly (bReadOnly);
    return this;
  }

  @NonNull
  public final EBootstrap5DateTimePickerMode getMode ()
  {
    return m_eMode;
  }

  /**
   * Set the overall mode. By default DATE is selected. This implies, that the default format for
   * the locale (as specified in the constructor is used). If you don't like the default, manually
   * set the format but this should not be necessary.
   *
   * @param eMode
   *        Mode to use. May not be <code>null</code>.
   * @return this for chaining
   * @see #setFormat(String)
   */
  @NonNull
  public final BootstrapDateTimePicker setMode (@NonNull final EBootstrap5DateTimePickerMode eMode)
  {
    ValueEnforcer.notNull (eMode, "Mode");
    m_eMode = eMode;
    return this;
  }

  @Nullable
  public final String getFormat ()
  {
    return m_sFormat;
  }

  /**
   * Set the format string to be used. This is only necessary, if the default one from
   * {@link #setMode(EBootstrap5DateTimePickerMode)} is not applicable. The format string must use
   * the Tempus Dominus v6 tokens (see {@link ETempusDominusFormatToken}).
   *
   * @param sFormat
   *        Format string to be used. May be <code>null</code>.
   * @return this for chaining
   */
  @NonNull
  public final BootstrapDateTimePicker setFormat (@Nullable final String sFormat)
  {
    m_sFormat = sFormat;
    return this;
  }

  @NonNull
  public final ETriState getShowCalendarWeeks ()
  {
    return m_eShowCalendarWeeks;
  }

  @NonNull
  public final BootstrapDateTimePicker setShowCalendarWeeks (final boolean bShowCalendarWeeks)
  {
    return setShowCalendarWeeks (ETriState.valueOf (bShowCalendarWeeks));
  }

  @NonNull
  public final BootstrapDateTimePicker setShowCalendarWeeks (@NonNull final ETriState eShowCalendarWeeks)
  {
    ValueEnforcer.notNull (eShowCalendarWeeks, "ShowCalendarWeeks");
    m_eShowCalendarWeeks = eShowCalendarWeeks;
    return this;
  }

  @NonNull
  public final ETriState getShowToday ()
  {
    return m_eShowToday;
  }

  @NonNull
  public final BootstrapDateTimePicker setShowToday (final boolean bShowToday)
  {
    return setShowToday (ETriState.valueOf (bShowToday));
  }

  @NonNull
  public final BootstrapDateTimePicker setShowToday (@NonNull final ETriState eShowToday)
  {
    ValueEnforcer.notNull (eShowToday, "ShowToday");
    m_eShowToday = eShowToday;
    return this;
  }

  @NonNull
  public final ETriState getShowClear ()
  {
    return m_eShowClear;
  }

  @NonNull
  public final BootstrapDateTimePicker setShowClear (final boolean bShowClear)
  {
    return setShowClear (ETriState.valueOf (bShowClear));
  }

  @NonNull
  public final BootstrapDateTimePicker setShowClear (@NonNull final ETriState eShowClear)
  {
    ValueEnforcer.notNull (eShowClear, "ShowClear");
    m_eShowClear = eShowClear;
    return this;
  }

  @NonNull
  public final ETriState getShowClose ()
  {
    return m_eShowClose;
  }

  @NonNull
  public final BootstrapDateTimePicker setShowClose (final boolean bShowClose)
  {
    return setShowClose (ETriState.valueOf (bShowClose));
  }

  @NonNull
  public final BootstrapDateTimePicker setShowClose (@NonNull final ETriState eShowClose)
  {
    ValueEnforcer.notNull (eShowClose, "ShowClose");
    m_eShowClose = eShowClose;
    return this;
  }

  @Nullable
  public final EBootstrap5DateTimePickerViewModeType getViewMode ()
  {
    return m_eViewMode;
  }

  @NonNull
  public final BootstrapDateTimePicker setViewMode (@Nullable final EBootstrap5DateTimePickerViewModeType eViewMode)
  {
    m_eViewMode = eViewMode;
    return this;
  }

  /**
   * Show date and time picker side by side?
   *
   * @return Never <code>null</code>
   */
  @NonNull
  public final ETriState getSideBySide ()
  {
    return m_eSideBySide;
  }

  @NonNull
  public final BootstrapDateTimePicker setSideBySide (final boolean bSideBySide)
  {
    m_eSideBySide = ETriState.valueOf (bSideBySide);
    return this;
  }

  @Nullable
  public final LocalDateTime getInitialDate ()
  {
    return m_aInitialDate;
  }

  @NonNull
  public final BootstrapDateTimePicker setInitialDate (@Nullable final LocalTime aInitialTime)
  {
    ValueEnforcer.isTrue (m_eMode == EBootstrap5DateTimePickerMode.TIME, "Current action mode is not TIME");
    if (aInitialTime == null)
      m_aInitialDate = null;
    else
      m_aInitialDate = DUMMY_DATE.atTime (aInitialTime);
    return this;
  }

  @NonNull
  public final BootstrapDateTimePicker setInitialDate (@Nullable final LocalDate aInitialDate)
  {
    ValueEnforcer.isTrue (m_eMode == EBootstrap5DateTimePickerMode.DATE, "Current action mode is not DATE");
    if (aInitialDate == null)
      m_aInitialDate = null;
    else
      m_aInitialDate = aInitialDate.atTime (DUMMY_TIME);
    return this;
  }

  @NonNull
  public final BootstrapDateTimePicker setInitialDate (@Nullable final LocalDateTime aInitialDateTime)
  {
    ValueEnforcer.isTrue (m_eMode == EBootstrap5DateTimePickerMode.DATE_TIME, "Current action mode is not DATE_TIME");
    m_aInitialDate = aInitialDateTime;
    return this;
  }

  @Nullable
  public final LocalDateTime getMinDate ()
  {
    return m_aMinDate;
  }

  @NonNull
  public final BootstrapDateTimePicker setMinDate (@Nullable final LocalTime aMinTime)
  {
    ValueEnforcer.isTrue (m_eMode == EBootstrap5DateTimePickerMode.TIME, "Current action mode is not TIME");
    if (aMinTime == null)
      m_aMinDate = null;
    else
      m_aMinDate = DUMMY_DATE.atTime (aMinTime);
    return this;
  }

  @NonNull
  public final BootstrapDateTimePicker setMinDate (@Nullable final LocalDate aMinDate)
  {
    ValueEnforcer.isTrue (m_eMode == EBootstrap5DateTimePickerMode.DATE, "Current action mode is not DATE");
    if (aMinDate == null)
      m_aMinDate = null;
    else
      m_aMinDate = aMinDate.atTime (DUMMY_TIME);
    return this;
  }

  @NonNull
  public final BootstrapDateTimePicker setMinDate (@Nullable final LocalDateTime aMinDateTime)
  {
    ValueEnforcer.isTrue (m_eMode == EBootstrap5DateTimePickerMode.DATE_TIME, "Current action mode is not DATE_TIME");
    m_aMinDate = aMinDateTime;
    return this;
  }

  @Nullable
  public final LocalDateTime getMaxDate ()
  {
    return m_aMaxDate;
  }

  @NonNull
  public final BootstrapDateTimePicker setMaxDate (@Nullable final LocalTime aMaxTime)
  {
    ValueEnforcer.isTrue (m_eMode == EBootstrap5DateTimePickerMode.TIME, "Current action mode is not TIME");
    if (aMaxTime == null)
      m_aMaxDate = null;
    else
      m_aMaxDate = DUMMY_DATE.atTime (aMaxTime);
    return this;
  }

  @NonNull
  public final BootstrapDateTimePicker setMaxDate (@Nullable final LocalDate aMaxDate)
  {
    ValueEnforcer.isTrue (m_eMode == EBootstrap5DateTimePickerMode.DATE, "Current action mode is not DATE");
    if (aMaxDate == null)
      m_aMaxDate = null;
    else
      m_aMaxDate = aMaxDate.atTime (DUMMY_TIME);
    return this;
  }

  @NonNull
  public final BootstrapDateTimePicker setMaxDate (@Nullable final LocalDateTime aMaxDateTime)
  {
    ValueEnforcer.isTrue (m_eMode == EBootstrap5DateTimePickerMode.DATE_TIME, "Current action mode is not DATE_TIME");
    m_aMaxDate = aMaxDateTime;
    return this;
  }

  /**
   * On show, will set the picker to the current date/time?
   *
   * @return Never <code>null</code>
   */
  @NonNull
  public final ETriState getUseCurrent ()
  {
    return m_eUseCurrent;
  }

  @NonNull
  public final BootstrapDateTimePicker setUseCurrent (final boolean bUseCurrent)
  {
    return setUseCurrent (ETriState.valueOf (bUseCurrent));
  }

  @NonNull
  public final BootstrapDateTimePicker setUseCurrent (@NonNull final ETriState eUseCurrent)
  {
    ValueEnforcer.notNull (eUseCurrent, "UseCurrent");
    m_eUseCurrent = eUseCurrent;
    return this;
  }

  /**
   * @return The icon that is by default prepended to each date time picker input group. By default
   *         it is {@link #DEFAULT_PREPEND_ICON}. May also be <code>null</code>.
   */
  @Nullable
  public final IHCNode getPrependIcon ()
  {
    return m_aPrependIcon;
  }

  /**
   * Set the default icon that is prepended to each date time picker input group. Call this method
   * with <code>null</code> to avoid the default calendar icon to be drawn. The picker always opens
   * by clicking into the input element as well - the icon additionally allows to close it.
   *
   * @param aPrependIcon
   *        The prepend icon to be used. May be <code>null</code>.
   * @return this for chaining
   */
  @NonNull
  public final BootstrapDateTimePicker setPrependIcon (@Nullable final IHCNode aPrependIcon)
  {
    m_aPrependIcon = aPrependIcon;
    return this;
  }

  /**
   * @return The mutable icon map for the calendar. The key is defined by Tempus Dominus v6 and can
   *         be <code>time, date, up, down, previous, next, today, clear or close</code>. The value
   *         is the String that contains the CSS classes to be applied (e.g.
   *         <code>fa fa-calendar</code>).
   */
  @NonNull
  @ReturnsMutableObject
  public final ICommonsOrderedMap <String, String> icons ()
  {
    return m_aIcons;
  }

  @NonNull
  public static JSInvocation invoke (@NonNull final IJSExpression aTargetElement,
                                     @NonNull final JSAssocArray aOptions)
  {
    // new tempusDominus.TempusDominus (element, options)
    return new JSInvocation (JSExpr.ref (JSExpr.ref ("tempusDominus"), "TempusDominus")).arg (aTargetElement)
                                                                                        .arg (aOptions);
  }

  @NonNull
  public final JSInvocation invoke ()
  {
    return invoke (JSHtml.documentGetElementById (getID ()), getJSOptions ());
  }

  private void _add (@NonNull final JSAssocArray ret,
                     @NonNull final String sKey,
                     @NonNull final EBootstrap5DateTimePickerTexts eText)
  {
    final String sValue = eText.getDisplayText (m_aDisplayLocale);
    if (StringHelper.isNotEmpty (sValue))
      ret.add (sKey, sValue);
  }

  /**
   * @return A {@link JSAssocArray} with all UI texts for the "localization" options of Tempus
   *         Dominus. Never <code>null</code>.
   */
  @NonNull
  public JSAssocArray getJSLocalizationTexts ()
  {
    final JSAssocArray ret = new JSAssocArray ();
    _add (ret, "today", EBootstrap5DateTimePickerTexts.TODAY);
    _add (ret, "clear", EBootstrap5DateTimePickerTexts.CLEAR);
    _add (ret, "close", EBootstrap5DateTimePickerTexts.CLOSE);
    _add (ret, "selectMonth", EBootstrap5DateTimePickerTexts.SELECT_MONTH);
    _add (ret, "previousMonth", EBootstrap5DateTimePickerTexts.PREV_MONTH);
    _add (ret, "nextMonth", EBootstrap5DateTimePickerTexts.NEXT_MONTH);
    _add (ret, "selectYear", EBootstrap5DateTimePickerTexts.SELECT_YEAR);
    _add (ret, "previousYear", EBootstrap5DateTimePickerTexts.PREV_YEAR);
    _add (ret, "nextYear", EBootstrap5DateTimePickerTexts.NEXT_YEAR);
    _add (ret, "selectDecade", EBootstrap5DateTimePickerTexts.SELECT_DECADE);
    _add (ret, "previousDecade", EBootstrap5DateTimePickerTexts.PREV_DECADE);
    _add (ret, "nextDecade", EBootstrap5DateTimePickerTexts.NEXT_DECADE);
    _add (ret, "previousCentury", EBootstrap5DateTimePickerTexts.PREV_CENTURY);
    _add (ret, "nextCentury", EBootstrap5DateTimePickerTexts.NEXT_CENTURY);
    _add (ret, "pickHour", EBootstrap5DateTimePickerTexts.PICK_HOUR);
    _add (ret, "incrementHour", EBootstrap5DateTimePickerTexts.INCREMENT_HOUR);
    _add (ret, "decrementHour", EBootstrap5DateTimePickerTexts.DECREMENT_HOUR);
    _add (ret, "pickMinute", EBootstrap5DateTimePickerTexts.PICK_MINUTE);
    _add (ret, "incrementMinute", EBootstrap5DateTimePickerTexts.INCREMENT_MINUTE);
    _add (ret, "decrementMinute", EBootstrap5DateTimePickerTexts.DECREMENT_MINUTE);
    _add (ret, "pickSecond", EBootstrap5DateTimePickerTexts.PICK_SECOND);
    _add (ret, "incrementSecond", EBootstrap5DateTimePickerTexts.INCREMENT_SECOND);
    _add (ret, "decrementSecond", EBootstrap5DateTimePickerTexts.DECREMENT_SECOND);
    _add (ret, "toggleMeridiem", EBootstrap5DateTimePickerTexts.TOGGLE_MERIDIEM);
    _add (ret, "selectTime", EBootstrap5DateTimePickerTexts.SELECT_TIME);
    _add (ret, "selectDate", EBootstrap5DateTimePickerTexts.SELECT_DATE);
    return ret;
  }

  @NonNull
  private static JSInvocation _asJSDate (@NonNull final LocalDateTime aDT)
  {
    // The JS Date constructor uses 0-based months
    return JSPrimitiveTypes.DATE._new (JSExpr.lit (aDT.getYear ()),
                                       JSExpr.lit (aDT.getMonthValue () - 1),
                                       JSExpr.lit (aDT.getDayOfMonth ()),
                                       JSExpr.lit (aDT.getHour ()),
                                       JSExpr.lit (aDT.getMinute ()),
                                       JSExpr.lit (aDT.getSecond ()));
  }

  /**
   * @return A {@link JSAssocArray} with all options for this date and time Picker. Never
   *         <code>null</code>.
   */
  @NonNull
  public JSAssocArray getJSOptions ()
  {
    final JSAssocArray aOptions = new JSAssocArray ();

    // Explicit format present?
    final String sJSFormat = StringHelper.isNotEmpty (m_sFormat) ? m_sFormat : m_eMode.getJSFormat (m_aDisplayLocale);

    // Display options
    final JSAssocArray aDisplay = new JSAssocArray ();

    if (m_aIcons.isNotEmpty ())
    {
      final JSAssocArray aIcons = new JSAssocArray ();
      for (final Map.Entry <String, String> aEntry : m_aIcons.entrySet ())
        aIcons.add (aEntry.getKey (), aEntry.getValue ());
      aDisplay.add ("icons", aIcons);
    }

    if (m_eSideBySide.isDefined ())
      aDisplay.add ("sideBySide", m_eSideBySide.getAsBooleanValue ());

    if (m_eShowCalendarWeeks.isDefined ())
      aDisplay.add ("calendarWeeks", m_eShowCalendarWeeks.getAsBooleanValue ());

    if (m_eViewMode != null)
      aDisplay.add ("viewMode", m_eViewMode.getJSValueString ());

    final JSAssocArray aComponents = new JSAssocArray ();
    aComponents.add ("calendar", m_eMode.isDateContained ());
    aComponents.add ("clock", m_eMode.isTimeContained ());
    if (sJSFormat.indexOf ('s') >= 0)
      aComponents.add ("seconds", true);
    aDisplay.add ("components", aComponents);

    if (m_eShowToday.isDefined () || m_eShowClear.isDefined () || m_eShowClose.isDefined ())
    {
      final JSAssocArray aButtons = new JSAssocArray ();
      if (m_eShowToday.isDefined ())
        aButtons.add ("today", m_eShowToday.getAsBooleanValue ());
      if (m_eShowClear.isDefined ())
        aButtons.add ("clear", m_eShowClear.getAsBooleanValue ());
      if (m_eShowClose.isDefined ())
        aButtons.add ("close", m_eShowClose.getAsBooleanValue ());
      aDisplay.add ("buttons", aButtons);
    }

    aOptions.add ("display", aDisplay);

    // Localization options
    final JSAssocArray aLocalization = getJSLocalizationTexts ();
    aLocalization.add ("locale", m_aDisplayLocale.toLanguageTag ());
    aLocalization.add ("format", sJSFormat);

    // Align the clock display with the effective format
    if (sJSFormat.indexOf ('H') >= 0)
      aLocalization.add ("hourCycle", "h23");
    else
      if (sJSFormat.indexOf ('h') >= 0)
        aLocalization.add ("hourCycle", "h12");

    aOptions.add ("localization", aLocalization);

    // Restrictions options
    if (m_aMinDate != null || m_aMaxDate != null)
    {
      final JSAssocArray aRestrictions = new JSAssocArray ();
      if (m_aMinDate != null)
        aRestrictions.add ("minDate", _asJSDate (m_aMinDate));
      if (m_aMaxDate != null)
        aRestrictions.add ("maxDate", _asJSDate (m_aMaxDate));
      aOptions.add ("restrictions", aRestrictions);
    }

    // Set before min, max and initial!
    if (m_eUseCurrent.isDefined ())
      aOptions.add ("useCurrent", m_eUseCurrent.getAsBooleanValue ());

    // Default date present?
    if (m_aInitialDate != null)
      aOptions.add ("defaultDate", _asJSDate (m_aInitialDate));

    // Clicking into or focusing the input opens the picker as well - like in the Bootstrap 4
    // version. Without a prepend icon this must NOT be set, because then the whole input group
    // acts as the toggle and the two handlers would cancel each other out.
    if (m_aPrependIcon != null)
      aOptions.add ("allowInputToggle", true);

    return aOptions;
  }

  @Override
  protected void onFinalizeNodeState (@NonNull final IHCConversionSettingsToNode aConversionSettings,
                                      @NonNull final IHCHasChildrenMutable <?, ? super IHCNode> aTargetNode)
  {
    if (m_aPrependIcon != null)
    {
      // The whole toggle span opens and closes the picker. Without a toggle element, Tempus
      // Dominus uses the whole input group as toggle - in that case the "data-td-target-toggle"
      // attribute may not be present, otherwise initialization fails.
      customAttrs ().setDataAttr ("td-target-toggle", "nearest");

      final HCSpan aToggle = new HCSpan ().addClass (CBootstrapCSS.INPUT_GROUP_TEXT);
      aToggle.customAttrs ().setDataAttr ("td-target", "#" + getID ());
      aToggle.customAttrs ().setDataAttr ("td-toggle", "datetimepicker");
      aToggle.addChild (m_aPrependIcon);
      addChildPrefixAtFront (aToggle);
    }

    super.onFinalizeNodeState (aConversionSettings, aTargetNode);
    addClass (CSS_CLASS_DATE);

    // Add JS if necessary
    if (!m_aEdit.isReadOnly ())
      addChild (new Bootstrap5DateTimePickerJS (this));
  }

  @Override
  protected void onRegisterExternalResources (@NonNull final IHCConversionSettingsToNode aConversionSettings,
                                              final boolean bForceRegistration)
  {
    super.onRegisterExternalResources (aConversionSettings, bForceRegistration);
    registerResourcesForThisRequest ();
  }

  public static void registerResourcesForThisRequest ()
  {
    // Popper must be present as global "window.Popper" before Tempus Dominus shows a popup
    PhotonJS.registerJSIncludeForThisRequest (EBootstrapUICtrlsJSPathProvider.POPPER);
    PhotonJS.registerJSIncludeForThisRequest (EBootstrapUICtrlsJSPathProvider.DATETIMEPICKER);

    EFontAwesome5Icon.registerResourcesForThisRequest ();
    PhotonCSS.registerCSSIncludeForThisRequest (EBootstrapUICtrlsCSSPathProvider.DATETIMEPICKER);
  }

  @NonNull
  public static BootstrapDateTimePicker create (@NonNull final String sName,
                                                @Nullable final LocalDate aInitialValue,
                                                @NonNull final Locale aDisplayLocale)
  {
    return new BootstrapDateTimePicker (sName,
                                        aInitialValue == null ? null : aInitialValue.atTime (DUMMY_TIME),
                                        aDisplayLocale,
                                        EBootstrap5DateTimePickerMode.DATE);
  }

  @NonNull
  public static BootstrapDateTimePicker create (@NonNull final String sName,
                                                @Nullable final LocalTime aInitialValue,
                                                @NonNull final Locale aDisplayLocale)
  {
    return new BootstrapDateTimePicker (sName,
                                        aInitialValue == null ? null : DUMMY_DATE.atTime (aInitialValue),
                                        aDisplayLocale,
                                        EBootstrap5DateTimePickerMode.TIME);
  }

  @NonNull
  public static BootstrapDateTimePicker create (@NonNull final String sName,
                                                @Nullable final LocalDateTime aInitialValue,
                                                @NonNull final Locale aDisplayLocale)
  {
    return new BootstrapDateTimePicker (sName, aInitialValue, aDisplayLocale, EBootstrap5DateTimePickerMode.DATE_TIME);
  }

  @NonNull
  public static BootstrapDateTimePicker create (@NonNull final String sName,
                                                @NonNull final Locale aDisplayLocale,
                                                @NonNull final EBootstrap5DateTimePickerMode eMode)
  {
    return new BootstrapDateTimePicker (sName, null, aDisplayLocale, eMode);
  }
}
