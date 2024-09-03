
# App Shortcuts

This project demonstrates how to implement App Shortcuts in an Android application. App Shortcuts allow users to quickly start specific actions in your app, providing a more efficient user experience.

| Static | Dynamic | Pinned |
| ------ | ------- | ------ |
| ![static](https://github.com/user-attachments/assets/86a9e520-4fe6-4dea-a101-bdade8588074) | ![dynamic](https://github.com/user-attachments/assets/974e7bf9-07fa-486f-8c62-411a5842a47d) | ![pinned](https://github.com/user-attachments/assets/c0eefa3f-9d69-4b8b-854d-14ba95578d76) |

## What are App Shortcuts?

App Shortcuts are a feature in Android that allow you to define specific actions or destinations within your app that users can access directly from their home screen or launcher. There are three types of App Shortcuts:

- **Static Shortcuts**: Defined in your app's resources and remain constant.
- **Dynamic Shortcuts**: Created and modified at runtime based on user interaction or other app logic.
- **Pinned Shortcuts**: Added by the user to the home screen, and can be updated or removed by the app.

## Static Shortcut

To define a static shortcut, you need to do two things:

1. **Create an XML file**: Define the static shortcuts in an XML resource file located in the `res/xml/` directory.
   `res/xml/shortcuts.xml`:
   ```xml
   <shortcuts xmlns:android="http://schemas.android.com/apk/res/android">
    <shortcut
        android:shortcutId="static"
        android:enabled="true"
        android:icon="@drawable/baseline_alarm_24"
        android:shortcutShortLabel="@string/short_label"
        android:shortcutLongLabel="@string/long_label"
        android:shortcutDisabledMessage="@string/disabled_message">
        <intent
            android:action="android.intent.action.VIEW"
            android:targetPackage="com.abdulkadirkara.appshortcuts"
            android:targetClass="com.abdulkadirkara.appshortcuts.MainActivity">
            <extra
                android:name="shortcut_id"
                android:value="static" />
        </intent>
    </shortcut>
</shortcuts>
```

2. **Configure the manifest**: Add the shortcut metadata in your `AndroidManifest.xml` file.

    ```xml
   <application ...>
    <meta-data
        android:name="android.app.shortcuts"
        android:resource="@xml/shortcuts" />
</application>
```

## Dynamic Shortcut

Dynamic shortcuts are created at runtime. Below is a method to add a dynamic shortcut:

```kotlin
private fun addDynamicShortCut(){
        val shortcut = ShortcutInfoCompat.Builder(this, "id")
            .setShortLabel("Call Mum")
            .setLongLabel("Clicking this will call your mum")
            .setIcon(IconCompat.createWithResource(this, R.drawable.baseline_call_24))
            .setIntent(
                Intent(this, MainActivity::class.java).apply {
                    action = Intent.ACTION_VIEW
                    putExtra("shortcut_id", "dynamic")
                })
            .build()

        ShortcutManagerCompat.pushDynamicShortcut(this, shortcut)

    }
    ```

This function creates a dynamic shortcut with the following steps:

1. ShortcutInfoCompat.Builder is used to build the shortcut.
2. setShortLabel and setLongLabel set the labels for the shortcut.
3. setIcon sets the icon for the shortcut.
4. setIntent defines the action and the target activity for the shortcut.
5. Finally, ShortcutManagerCompat.pushDynamicShortcut is called to add the shortcut.

## Pinned Shortcut
Pinned shortcuts are similar to dynamic shortcuts, but they are pinned to the user's home screen and require user confirmation. Here's how to create a pinned shortcut:

```kotlin
private fun addPinnedShortcut() {
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return
        }

        val shortcutManager = getSystemService<ShortcutManager>()!!
        if(shortcutManager.isRequestPinShortcutSupported) {
            val shortcut = ShortcutInfo.Builder(applicationContext, "pinned")
                .setShortLabel("Send message")
                .setLongLabel("This sends a message to a friend")
                .setIcon(
                    Icon.createWithResource(
                    applicationContext, R.drawable.baseline_push_pin_24
                ))
                .setIntent(
                    Intent(applicationContext, MainActivity::class.java).apply {
                        action = Intent.ACTION_VIEW
                        putExtra("shortcut_id", "pinned")
                    }
                )
                .build()

            val callbackIntent = shortcutManager.createShortcutResultIntent(shortcut)
            val successPendingIntent = PendingIntent.getBroadcast(
                applicationContext,
                0,
                callbackIntent,
                PendingIntent.FLAG_IMMUTABLE
            )
            shortcutManager.requestPinShortcut(shortcut, successPendingIntent.intentSender)
        }
    }
```

This function does the following:

Checks if the API level supports pinned shortcuts.
Creates a ShortcutInfo object with details such as labels, icon, and intent.
Uses ShortcutManager.requestPinShortcut to request pinning the shortcut to the home screen. A PendingIntent is used to handle the result of the request.
