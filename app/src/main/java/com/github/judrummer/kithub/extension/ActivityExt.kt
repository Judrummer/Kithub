package com.github.judrummer.kithub.extension

import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentTransaction

fun FragmentActivity.fragmentTransaction(transactionBlock: (FragmentTransaction).() -> (Unit)) {
    val transaction = supportFragmentManager.beginTransaction()
    transaction.transactionBlock()
    transaction.commit()
}


