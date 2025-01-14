# Twitter's Jetpack Compose Rules

A big challenge to face when a big team with a large codebase starts adopting Compose is that not everybody will start at the same page. This happened to us at Twitter.

Compose is 🔝, allows for amazing things, but has a bunch of footguns to be aware of. [See the thread](https://twitter.com/mrmans0n/status/1507390768796909571).

This is where these static checks come in. We want to detect all the potential issues even before they reach the code review state, and foster a healthy Compose adoption.

Check out the project website for more information: https://twitter.github.io/compose-rules

## Static checks for Compose bundled in this repo

The comprehensive list of what these rules will check for is in [the rules documentaton](https://github.com/twitter/compose-rules/blob/main/docs/rules.md). It contains both what we check for and why are we doing that, so giving it a good read is encouraged.

## Using with ktlint

```groovy
dependencies {
    ktlintRuleset "com.twitter.compose.rules:ktlint:<VERSION>"
}
```

For more information, you can refer to the [Using with ktlint](https://twitter.github.io/compose-rules/#using-the-custom-ruleset-with-ktlint) documentation.

### Using with Spotless

If using [Spotless](https://github.com/diffplug/spotless), you can use these rules via ktlint. There doesn't seem to be official support for this yet, but there is a workaround explained [in this issue](https://github.com/diffplug/spotless/issues/1220).

## Using with Detekt

```groovy
dependencies {
    detektPlugins "com.twitter.compose.rules:detekt:<VERSION>"
}
```

For more information, you can refer to the [Using with Detekt](https://twitter.github.io/compose-rules/#using-the-custom-ruleset-with-detekt) documentation.

## Contributing

Contributing new rules or fixes is welcome. See the [Contributing](CONTRIBUTING.md) instructions for more information.

## License

```
    Copyright 2022 Twitter, Inc.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
```
