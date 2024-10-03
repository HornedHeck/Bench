#!/usr/bin/zsh

set -e
#test_type=$1
test_type="COMPOSE"
#compilation_types=("Interpret")
compilation_types=("AOT" "JIT" "Default" "Interpret")

RESULTS_PATH="app/benchmark/build/outputs/connected_android_test_additional_output/benchmark/connected"

mkdir -p "./output"
rm -rf "./output/$test_type"
mkdir "./output/$test_type"

./gradlew clean
./gradlew :app:assemble
./gradlew :app:benchmark:assemble

wait_online() {
  sleep 10
  incr=1
  while [[ true ]];
    do
      if $(adb shell getprop service.adb); then break; fi
      if [[ "$incr" -gt "60" ]]; then break; fi;
         ((incr++));
         sleep 5;
         echo $incr;
        done
}

for compilation_type in $compilation_types
do
  echo "Running $test_type for $compilation_type compilation mode"
  echo " * Reboot device"
  adb reboot
  adb wait-for-device

  wait_online
  adb root
  wait_online



# Install apks
  echo " * Installing APKs"
  adb install app/build/outputs/apk/benchmark/app-benchmark.apk
  adb install app/benchmark/build/outputs/apk/benchmark/benchmark-benchmark.apk

#  Disable JIT compilation on the device
  if [[ $compilation_type == "Interpret" ]]; then
    echo " * Disable JIT compilation on the device"
      adb shell setprop dalvik.vm.extra-opts -Xusejit:false
      adb shell stop
      adb shell start
      wait_online
  fi

#  Lock frequencies
  ./lockClocks.sh

#  Run instrumentation test
  echo " * Run instrumentation"
  adb shell am instrument -w \
    -e class com.hornedheck.benchmark.MacroBenchmark#parametrized \
    -e type $test_type \
    -e compilation_mode $compilation_type \
    -e execution_time 10000 \
    -e "androidx.benchmark.suppressErrors" "EMULATOR,DEBUGGABLE" \
    -e "androidx.benchmark.enabledRules" "baselineprofile,macrobenchmark" \
    com.hornedheck.benchmark/androidx.test.runner.AndroidJUnitRunner

#  Copy results to separate folder
  echo " * Copy results"
  adb pull /storage/emulated/0/Android/media/com.hornedheck.benchmark "output/$test_type/$compilation_type"

# Clear app installations
  echo " * Clean up"
  adb uninstall "com.hornedheck.bench"
  adb uninstall "com.hornedheck.benchmark"

done