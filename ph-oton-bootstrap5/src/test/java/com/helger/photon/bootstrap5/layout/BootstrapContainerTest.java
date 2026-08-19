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
package com.helger.photon.bootstrap5.layout;

import static com.helger.photon.bootstrap5.mock.BootstrapTestHelper.getAsHTMLString;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Test class for class {@link BootstrapContainer}.
 *
 * @author Philip Helger
 */
public final class BootstrapContainerTest
{
  @Test
  public void testFixed ()
  {
    assertEquals ("<div class=\"container\">Text</div>", getAsHTMLString (new BootstrapContainer ().addChild ("Text")));
  }

  @Test
  public void testFluid ()
  {
    assertEquals ("<div class=\"container-fluid\">Text</div>",
                  getAsHTMLString (new BootstrapContainer (true).addChild ("Text")));
    assertEquals ("<div class=\"container-fluid\">Text</div>",
                  getAsHTMLString (new BootstrapContainer ().setFluid (true).addChild ("Text")));
  }
}
