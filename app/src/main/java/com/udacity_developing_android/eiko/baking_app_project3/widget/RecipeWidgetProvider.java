package com.udacity_developing_android.eiko.baking_app_project3.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;

import com.udacity_developing_android.eiko.baking_app_project3.R;
import com.udacity_developing_android.eiko.baking_app_project3.Recipe;
import com.udacity_developing_android.eiko.baking_app_project3.RecipeDetailActivity;

import java.util.ArrayList;
import java.util.Arrays;

public class RecipeWidgetProvider extends AppWidgetProvider {

    private static final String DIVIDER = ":";
    private static String recipeName = "";
    private static String recipeIngredient = "";
    private static ArrayList<String> ingredientList;

//    public RecipeWidgetProvider(Context applicationContext,
//                                AppWidgetManager appWidgetManager,
//                                Intent intent) {
//    }

    static void updateAppwidget(Context context,
                                AppWidgetManager appWidgetManager,
                                int appwidgetId) {
        Recipe recipeAvailable = ConfigRecipeWidget.
                getCurrentRecipePreference(context, appwidgetId);
        String recipewidgetDetail = ConfigRecipeWidget.
                getRecipeDetailPreference(context, appwidgetId);

        if (recipewidgetDetail == null) {
            return;
        }

        if (recipewidgetDetail.contains(DIVIDER)) {
            String[] parts = recipewidgetDetail.split(DIVIDER);
            recipeName = parts[0];
            recipeIngredient = parts[1];
        }
        Log.i("Provider1", recipewidgetDetail.toString());
        Log.i("Provider2", recipeName);
        Log.i("Provider3", recipeIngredient);
        ingredientList = new ArrayList<>(Arrays.asList(
                recipeIngredient));
        RemoteViews views = new RemoteViews(context.getPackageName(),
                R.layout.recipe_widget_provider);
        views.setTextViewText(R.id.widget_recipe_text_name, recipeName);
//        views.setTextViewText(R.id.widget_recipe_ingredient_list,
//                recipeIngredient);

//        String image = (recipeName.replaceAll("\\s+", ""))
//                .toLowerCase();
//        int imageResId = context.getResources().getIdentifier(
//                image,"drawable", context.getPackageName());
//        views.setImageViewResource(R.id.widget_recipe_image, imageResId);

//Adding widget listview.
        SharedPreferences sp = context.getApplicationContext().getSharedPreferences(
                "RecipeWidget", 0);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("KONDATE", recipeIngredient);
        editor.apply();
        Intent intent_list = new Intent(context, ListRemoteviewFactory.class);
        intent_list.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appwidgetId);
        intent_list.putExtra("ingrediendlist", ingredientList.toString());
        views.setRemoteAdapter(R.id.widget_listview, intent_list);
        Bundle bundle = new Bundle();
        bundle.putParcelable("RECIPE_DETAIL_INFORMATION", recipeAvailable);
//        bundle.putParcelableArrayList(ingredientList);
        Log.i("Provider4", recipeAvailable.toString());
//        //Adding widget listview.
//        Intent intent_list = new Intent(context, ListRemoteviewFactory.class);
//        intent_list.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appwidgetId);
//        views.setRemoteAdapter(R.id.widget_listview, intent_list);

        Intent intent = new Intent(context, RecipeDetailActivity.class);
        intent.putExtras(bundle);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.widget_recipe_card, pendingIntent);

        appWidgetManager.updateAppWidget(appwidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {
        for (int appwidgetId : appWidgetIds)
            updateAppwidget(context, appWidgetManager, appwidgetId);
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            ConfigRecipeWidget.deleteRecipePreference(
                    context, appWidgetId);
        }
    }
}
