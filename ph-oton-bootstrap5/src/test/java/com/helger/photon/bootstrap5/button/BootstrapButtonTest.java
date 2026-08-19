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
package com.helger.photon.bootstrap5.button;

import static com.helger.photon.bootstrap5.mock.BootstrapTestHelper.getAsHTMLString;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Test class for class {@link BootstrapButton}.
 *
 * @author Philip Helger
 */
public final class BootstrapButtonTest
{
  @Test
  public void testTypeAndSize ()
  {
    assertEquals ("<button class=\"btn btn-primary btn-lg\" type=\"button\">Go</button>",
                  getAsHTMLString (new BootstrapButton (EBootstrapButtonType.PRIMARY, EBootstrapButtonSize.LARGE)
                                                                                                                 .addChild ("Go")));
    assertEquals ("<button class=\"btn btn-outline-danger btn-sm\" type=\"button\">Go</button>",
                  getAsHTMLString (new BootstrapButton (EBootstrapButtonType.OUTLINE_DANGER, EBootstrapButtonSize.SMALL)
                                                                                                                        .addChild ("Go")));
  }

  @Test
  public void testSubmitButton ()
  {
    assertEquals ("<button class=\"btn btn-primary\" type=\"submit\">Send</button>",
                  getAsHTMLString (new BootstrapSubmitButton ().addChild ("Send")));
  }

  @Test
  public void testResetButton ()
  {
    assertEquals ("<button class=\"btn btn-outline-secondary\" type=\"reset\">Reset</button>",
                  getAsHTMLString (new BootstrapResetButton ().addChild ("Reset")));
  }
}
