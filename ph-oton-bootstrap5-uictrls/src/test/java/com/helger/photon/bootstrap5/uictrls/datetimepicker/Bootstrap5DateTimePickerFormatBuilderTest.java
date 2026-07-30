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

import static org.junit.Assert.assertEquals;

import org.jspecify.annotations.NonNull;
import org.junit.Test;

/**
 * Test class for class {@link Bootstrap5DateTimePickerFormatBuilder}.
 *
 * @author Philip Helger
 */
public final class Bootstrap5DateTimePickerFormatBuilderTest
{
  @NonNull
  private static String _getAsJS (@NonNull final String s)
  {
    return Bootstrap5DateTimePickerFormatBuilder.fromJavaPattern (s).getJSCalendarFormatString ();
  }

  @Test
  public void testBasic ()
  {
    assertEquals ("dd.MM.yyyy", _getAsJS ("dd.MM.yyyy"));
    assertEquals ("dd.MM.yyyy", _getAsJS ("dd.MM.uuuu"));
    assertEquals ("dd/MM", _getAsJS ("dd/MM"));
  }

  @Test
  public void testTime ()
  {
    assertEquals ("HH:mm:ss", _getAsJS ("HH:mm:ss"));
    assertEquals ("h:mm T", _getAsJS ("h:mm a"));
    assertEquals ("dd.MM.yyyy HH:mm:ss", _getAsJS ("dd.MM.yyyy HH:mm:ss"));
  }

  @Test
  public void testTextualTokens ()
  {
    assertEquals ("MMM d, y", _getAsJS ("MMM d, y"));
    assertEquals ("MMMM d, yyyy", _getAsJS ("MMMM d, yyyy"));
    assertEquals ("ddd", _getAsJS ("E"));
    assertEquals ("ddd", _getAsJS ("EEE"));
    assertEquals ("dddd", _getAsJS ("EEEE"));
  }
}
