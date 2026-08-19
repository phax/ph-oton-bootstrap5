# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## What this is

Java wrapper for Bootstrap 5.3.x on top of the ph-oton web framework (server-side HTML rendering, no templating engine). Maven multi-module, Java 17+, JakartaEE 10 (`jakarta.servlet`, never `javax.servlet`).

Modules, in dependency order — each depends on the previous one:

`ph-oton-bootstrap5` (component wrappers) → `-uictrls` (DataTables, DateTimePicker, TreeView, …) → `-pages` (ready-made admin/info pages) → `-stub` (servlet stub + SPI init) → `-demo` (WAR, not deployed to Maven Central)

## Build and test

* Build one module plus what it needs: `mvn -pl ph-oton-bootstrap5-uictrls -am install`
* Run the demo app: `main()` in `RunInJettyPHOTONDEMO_BS5` (test scope of `-demo`) → http://localhost:8080/ ; stop it with `JettyStopPHOTONDEMO_BS5`
* Single test: `mvn -pl <module> test -Dtest=BootstrapVanillaJSTest`

## Imports (ph-commons 12 layout)

Getting these wrong is the most common build break:

* Nullability: `org.jspecify.annotations.NonNull` / `.Nullable` — **not** `javax.annotation.*`
* `com.helger.annotation.Nonempty`, `com.helger.annotation.style.ReturnsMutableCopy`, `com.helger.annotation.concurrent.Immutable`
* Collections: `com.helger.collection.commons.CommonsArrayList` / `ICommonsList` / `CommonsLinkedHashMap`
* Base helpers: `com.helger.base.string.StringImplode`, `com.helger.base.version.Version`, `com.helger.base.thirdparty.*`
* `com.helger.commons.*` is the *old* layout and only survives in a leftover or two — do not add new imports from it

## Generated JS must be vanilla — no jQuery

Bootstrap 5 removed its jQuery plugin API. Every *Bootstrap* call this library emits must be vanilla: `document.querySelectorAll(...)`, `bootstrap.Tooltip` / `bootstrap.Modal` (`getOrCreateInstance`), and `data-bs-*` attributes — never `$(...).tooltip()`. `BootstrapVanillaJSTest` asserts the generated code contains no `$(`.

jQuery itself is *not* gone from the runtime: `PhotonStubInitializer` registers `EUICoreJSPathProvider.JQUERY_3` globally for ph-oton's own uicore JS, `ph-oton-jquery` supplies `JQuerySelector` as a selector-string source, and the default `IHCOnDocumentReadyProvider` wraps inline scripts in `$(document).ready(...)`. So a *rendered* script may legitimately contain `$` — assert on the JS body (`jsAttach ().getJSCode ()`), not on the rendered wrapper.

## Adding or updating a JS/CSS resource

The full chain — skipping a step makes the tests fail, not the compiler:

1. Put the file under `src/main/resources/external/<lib>/<version>/` (third-party) or `src/main/resources/ph-oton/` (own code).
2. Add the enum entry in `EBootstrapJSPathProvider` / `EBootstrapCSSPathProvider` (core) or `EBootstrapUICtrls{JS,CSS}PathProvider` (uictrls). They use `.minifiedPathFromPath()`, so a matching `*.min.js` / `*.min.css` **must** exist next to the source — `EBootstrap*PathProviderTest` asserts both variants resolve on the classpath.
3. Generate the `.min` file with the build (`ph-jscompress-maven-plugin` / `ph-csscompress-maven-plugin` run during `mvn install`) and commit it. Both plugins are configured with `forceCreation`/`forceCompress` = `false`, so an already up-to-date `.min` is not rebuilt — delete it to force regeneration.
4. For a third-party library, register it in that module's `ThirdPartyModuleProvider_*` class (`com.helger.photon.bootstrap5[.uictrls|.stub].config`) with license and version; `SPITest` validates the SPI wiring.

Bumping the Bootstrap version touches four places: the `external/bootstrap/<version>/` folder, the paths in both path provider enums, `CBootstrap.BOOTSTRAP_VERSION_*`, and `ThirdPartyModuleProvider_ph_oton_bootstrap5`.

## Code style

See the linked [Coding Styleguide](https://github.com/phax/meta/blob/master/CodingStyleguide.md). The non-obvious parts: Hungarian notation (`sName`, `aList`, `bFlag`, `m_` for members), a space before every `(` and `<` (`new Foo ()`, `ICommonsList <String>`), `final` on all parameters, `_` prefix on private methods, and `ID` always uppercase.

Every Java file starts with the Apache 2.0 header from `src/etc/license-template.txt` (`Copyright (C) 2025-2026 Philip Helger`). The inherited parent POM wires `com.mycila:license-maven-plugin` to that template, so `mvn -pl <module> license:format` applies the header to new files and `license:check` verifies it.

## Release notes

There is no wiki checkout for this repo. Record every public API change as a bullet under `**v1.0.0 - Work in Progress**` in the `## News and Noteworthy` section of `README.md`, matching the existing bullet style.
