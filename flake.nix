{
  description = "Scala development shell";

  inputs = {
    nixpkgs.url = github:nixos/nixpkgs/nixpkgs-unstable;
    flake-utils.url = github:numtide/flake-utils;
    flake-compat = {
      url = github:edolstra/flake-compat;
      flake = false;
    };
  };

  outputs = { self, nixpkgs, flake-utils, ... }:
    let

      forSystem = system:
        let
          pkgs = nixpkgs.legacyPackages.${system};
          jdk11 = pkgs.jdk11_headless;
          jdk = pkgs.jdk17_headless;
        in
        {
          devShell = pkgs.mkShell {
            name = "scala-dev-shell";
            buildInputs = [
              jdk11
              jdk
              pkgs.coursier
              pkgs.sbt
              pkgs.scala-cli
            ];
            shellHook = ''
              export JAVA_HOME="${jdk}"
              export JAVA11_HOME="${jdk11}"
            '';
          };
        };
    in
    flake-utils.lib.eachDefaultSystem forSystem;
}
