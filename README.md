# Zeta Sql Formatter

Little cli utility for formatting using Google [ZetaSql](https://github.com/google/zetasql)

Build with the estimable [scala-cli](https://scala-cli.virtuslab.org/)

Manual build is trivial if you don't want to nix.

Install scala-cli (see above link) for your platform.

Note use JDK 8 or 11 for packaging, later versions will likely not work due to ZetaSql JNI methods.

Simple local package build :

```bash
scala-cli package zetafmt.scala -f -o

```

Then use zetafmt command with either stdin or a filename argument and the formatted output is printed to stdout.

e.g.

```bash
zetafmt <good.sql
```

will output
```sql
SELECT
  t2.col1 AS t2_col1,
  t1.col2 AS t1_col2,
  t1.col3 AS t1_col3
FROM
  table1 AS t1
  JOIN
  table2 AS t2
  ON t1.col1 = t2.col1
WHERE
  t1.col2 = 'foo' AND t2.col1 = 5 AND t1.col3 IS NOT null
GROUP BY ROLLUP(t2_col1, t1_col2, 3)
ORDER BY 1, 2 DESC, t1_col3
LIMIT 100;
```

Local Docker builder - again using jdk 11 base image

```bash
scala-cli package zetafmt.scala --docker --docker-from adoptopenjdk/openjdk11 --docker-image-repository zeta-format
```

run with

```bash
docker run -i zeta-format:latest <good.sql
```

## Run On Nixos

If a jvm for bloop is not specified then `scala-cli` will download it which will not run on nixos due to [fhs](https://nixos.wiki/wiki/Packaging/Binaries).

Fortunately the smart devs of `scala-cli` allow you to specify pretty much anything necessary as command flags. JDK17 is required at the time of writing. See `flake.nix` for environment setup

```bash
# compile
scala-cli compile zetafmt.scala --bloop-jvm='system|17'
# package
scala-cli package zetafmt.scala -f -o zetafmt --java-home ${JAVA11_HOME}
# packge docker 
scala-cli package zetafmt.scala -f --docker --docker-from adoptopenjdk/openjdk11 --docker-image-repository zeta-format --java-home ${JAVA11_HOME}
```
