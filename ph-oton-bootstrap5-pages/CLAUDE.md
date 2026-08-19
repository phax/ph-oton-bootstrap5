# ph-oton-bootstrap5-pages

Ready-made admin and info pages (security, monitoring, settings, sysinfo, data, appinfo) rendered with Bootstrap 5. See the root `CLAUDE.md` for the repo-wide rules.

## Page conventions

* Pages extend `AbstractBootstrapWebPage`, `AbstractBootstrapWebPageForm` or `AbstractBootstrapWebPageSimpleForm`. All of them implement `IHCBootstrap5Trait`, so component factory methods (`badge (...)`, `error (...)`, `success (...)`) are available unqualified inside a page.
* Shared markup helpers live in `BootstrapWebPageUIHandler.INSTANCE` — use it instead of building headers/toolbars by hand.
* `BootstrapPagesMenuConfigurator` registers the standard menu entries; its `MENU_ADMIN_*` string constants are the menu item IDs applications reference. Adding a page means adding both the page class and its entry there.

## Texts are always bilingual

Every page keeps its own `protected enum EText implements IHasDisplayText` with German and English text, in that order:

```java
MSG_NAME ("Name", "Name"),
...
EText (final String sDE, final String sEN)
{
  m_aTP = TextHelper.create_DE_EN (sDE, sEN);
}
```

Never hard-code a display string in the page body — add an `EText` constant with both languages and resolve it via `getDisplayText (aDisplayLocale)`.

## Rendering

Pages need a full `IWebPageExecutionContext` (and therefore a web scope), so they are exercised through the demo application rather than through unit tests. `BootstrapForm` also requires an `ISimpleWebExecutionContext` in its constructor — that is why form rendering is not unit tested here; test the individual components in `ph-oton-bootstrap5` instead.
