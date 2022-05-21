package ru.alex009.moko.paging.myapplication.android

import android.content.Context
import android.view.View
import androidx.lifecycle.LifecycleOwner
import dev.icerock.moko.units.TableUnitItem
import dev.icerock.moko.units.viewbinding.VBTableUnitItem
import dev.icerock.moko.units.viewbinding.VBViewHolder
import ru.alex009.moko.paging.myapplication.android.databinding.LoadingUnitBinding

class LoadingUnitItem : VBTableUnitItem<LoadingUnitBinding>() {
    override val layoutId: Int = R.layout.loading_unit

    override fun bindView(view: View): LoadingUnitBinding = LoadingUnitBinding.bind(view)

    override fun LoadingUnitBinding.bindData(
        context: Context,
        lifecycleOwner: LifecycleOwner,
        viewHolder: VBViewHolder<LoadingUnitBinding>
    ) = Unit

    override val itemId: Long = TableUnitItem.NO_ID
}
