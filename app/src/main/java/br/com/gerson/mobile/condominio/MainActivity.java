package br.com.gerson.mobile.condominio;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import br.com.gerson.mobile.condominio.controller.CondominioController;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                handleMenuDrawer();
                super.onDrawerSlide(drawerView, slideOffset);
            }
        };
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_reservas) {
            Intent i = new Intent(this, ReservaActivity.class);
            this.startActivity(i);
        } else if (id == R.id.nav_pendentes) {
            Intent i = new Intent(this, PendentesActivity.class);
            this.startActivity(i);

        } else if (id == R.id.nav_login) {
            CondominioController condominioController = new CondominioController(this);
            if (condominioController.isLoggedIn()) {
                condominioController.logout();
            } else {
                Intent i = new Intent(this, LoginActivity.class);
                this.startActivity(i);
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void handleMenuDrawer() {
        CondominioController condominioController = new CondominioController(this);
        Boolean isLoggedIn = condominioController.isLoggedIn();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        Menu menu = navigationView.getMenu();
        MenuItem menuItem = menu.findItem(R.id.nav_login);
        String textoMenuItem = isLoggedIn ? getString(R.string.sair) : getString(R.string.entrar);
        menuItem.setTitle(textoMenuItem);
        MenuItem menuItemPendentes = menu.findItem(R.id.nav_pendentes);
        menuItemPendentes.setVisible(isLoggedIn && condominioController.isAdmin());
        MenuItem menuItemReservas = menu.findItem(R.id.nav_reservas);
        menuItemReservas.setVisible(isLoggedIn);
    }
}
