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

import java.util.Locale;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import com.helger.annotation.Nonempty;
import com.helger.annotation.misc.Translatable;
import com.helger.text.IMultilingualText;
import com.helger.text.display.IHasDisplayTextWithArgs;
import com.helger.text.resolve.DefaultTextResolver;
import com.helger.text.util.TextHelper;

/**
 * Texts used.
 *
 * @author Philip Helger
 */
@Translatable
public enum EBootstrap5DateTimePickerTexts implements IHasDisplayTextWithArgs
{
  TODAY ("Heute", "Go to today"),
  CLEAR ("Auswahl löschen", "Clear selection"),
  CLOSE ("Schließen", "Close the picker"),
  SELECT_MONTH ("Monat wählen", "Select Month"),
  PREV_MONTH ("Vorheriger Monat", "Previous Month"),
  NEXT_MONTH ("Nächster Monat", "Next Month"),
  SELECT_YEAR ("Jahr wählen", "Select Year"),
  PREV_YEAR ("Nächstes Jahr", "Previous Year"),
  NEXT_YEAR ("Vorheriges Jahr", "Next Year"),
  SELECT_DECADE ("Dekade wählen", "Select Decade"),
  PREV_DECADE ("Vorherige Dekade", "Previous Decade"),
  NEXT_DECADE ("Nächste Dekade", "Next Decade"),
  PREV_CENTURY ("Vorheriges Jahrhundert", "Previous Century"),
  NEXT_CENTURY ("Nächstes Jahrhundert", "Next Century"),
  PICK_HOUR ("Stunde wählen", "Pick Hour"),
  INCREMENT_HOUR ("Nächste Stunde", "Increment Hour"),
  DECREMENT_HOUR ("Vorherige Stunde", "Decrement Hour"),
  PICK_MINUTE ("Minute wählen", "Pick Minute"),
  INCREMENT_MINUTE ("Nächste Minute", "Increment Minute"),
  DECREMENT_MINUTE ("Vorherige Minute", "Decrement Minute"),
  PICK_SECOND ("Sekunde wählen", "Pick Second"),
  INCREMENT_SECOND ("Nächste Sekunde", "Increment Second"),
  DECREMENT_SECOND ("Vorherige Sekunde", "Decrement Second"),
  TOGGLE_MERIDIEM ("Tageszeit umschalten", "Toggle Meridiem"),
  SELECT_TIME ("Zeit wählen", "Select Time"),
  SELECT_DATE ("Datum wählen", "Select Date");

  private final IMultilingualText m_aTP;

  EBootstrap5DateTimePickerTexts (@NonNull @Nonempty final String sDE, @NonNull @Nonempty final String sEN)
  {
    m_aTP = TextHelper.create_DE_EN (sDE, sEN);
  }

  @Nullable
  public String getDisplayText (@NonNull final Locale aContentLocale)
  {
    return DefaultTextResolver.getTextStatic (this, m_aTP, aContentLocale);
  }

  @NonNull
  public IMultilingualText getMultilingualText ()
  {
    return m_aTP;
  }
}
