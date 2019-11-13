# Amazing Battery

A widget to show the battery level.

  - No permission required.
  - No ads.

## Technical Details

  - The widget does not use the built-in widget update event to update the
    battery level because the Android OS does not allow update interval of
    less than 30 minutes.

  - Instead it uses Alarm manager to update the battery level at one minute
    interval.  The Android OS may still regulate the alarm frequency though
    for power saving reason.

  - The battery percentage is shown with a shadow for it to stay visible
    in all background colors.

## Milestones

  - v0.3 UI/naming convention clean up
  - v0.4 battery level color code

