# ph-oton-bootstrap5-uictrls

Bootstrap 5 bindings for the richer ph-oton UI controls (DataTables, DateTimePicker, TreeView, Select2, Typeahead, Prism) plus the `ext` helpers. Builds on `ph-oton-bootstrap5`; see the root `CLAUDE.md` for the repo-wide rules.

## Per-request resource registration

Unlike the core module, components here pull in JS/CSS via `onRegisterExternalResources (...)`, e.g.

```java
EFontAwesome5Icon.registerResourcesForThisRequest ();
PhotonCSS.registerCSSIncludeForThisRequest (EBootstrapUICtrlsCSSPathProvider.BOOTSTRAP_EXT);
```

Those calls need a request scope, so **rendering tests in this module must install `@Rule public final PhotonAppWebTestRule m_aRule = new PhotonAppWebTestRule ();`** (from ph-oton-app). Without it the render fails with a missing-scope error. `BootstrapCardCollapsibleTest` is the pattern to copy.

## DateTimePicker (Tempus Dominus)

`BootstrapDateTimePicker` wraps Tempus Dominus 6.9.4 (`external/tempusdominus/6.9.4/`), which needs Popper 2 (`external/popperjs/2.11.8/`) for popup positioning — both are registered through `EBootstrapUICtrlsJSPathProvider`.

Version 6 is a full rewrite of the version 4 widget used by ph-oton-bootstrap4; do not port code from there. Specifics that are easy to get wrong:

* Initialization is `new tempusDominus.TempusDominus (element, options)` — not a jQuery plugin call.
* Java `DateTimeFormatter` patterns are translated to Tempus Dominus tokens by `Bootstrap5DateTimePickerFormatBuilder` / `ETempusDominusFormatToken` (e.g. Java `EEEE` → `dddd`, Java `a` → `T`). Add new tokens to the enum, and `ETempusDominusFormatTokenTest` verifies each one still round-trips through the builder.
* `Bootstrap5DateTimePickerSpecialNodeListModifier` merges the init code of all pickers on a page that share the same options, so that initialization JS is emitted once instead of per control.

## Third-party registration

Every vendored library here (Tempus Dominus, Popper, Quercus TreeView) must appear in `ThirdPartyModuleProvider_ph_oton_bootstrap5_uictrls` with its license and version, and needs a committed `.min` file — see the root `CLAUDE.md` resource chain.
