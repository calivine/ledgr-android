package com.example.ledgr.ui.widget

import android.graphics.Color
import com.example.ledgr.R
import com.github.aachartmodel.aainfographics.aachartcreator.*
import com.github.aachartmodel.aainfographics.aaoptionsmodel.*
import com.google.android.material.color.MaterialColors
import java.lang.Math.round

class LineChart {

    private val model = AAChartModel()
        .chartType(AAChartType.Line)
        .animationType(AAChartAnimationType.Elastic)
        .markerRadius(2f)
        .colorsTheme(arrayOf("#38C172"))
    private var data: List<Float>? = null
    private var labels: List<String>? = null
    private lateinit var options: AAOptions


    init {
        model.dataLabelsEnabled(true)
    }

    fun dataLabels(prop: Boolean): LineChart {
        model.dataLabelsEnabled(prop)
        return this
    }

    fun setDataAndLabels(prop: MutableMap<String, Float>): LineChart {
        data = prop.values.toList()
        labels = prop.keys.toList()
        return this
    }

    fun build(): LineChart {

        model.series(
            arrayOf(
                AASeriesElement()
                    .data(Array(data!!.size - 1) { i -> round(data!![i]) })
            )
        )

        options = AAOptionsConstructor.configureChartOptions(model)
        // options.chart!!.marginTop(50.0F)
        // options.chart!!.margin(arrayOf(45.0F, 35.0F, 45.0F, 35.0F))

        options.tooltip(AATooltip().useHTML(true).formatter("""
            function () {
                return '$' + this.y;
            }
        """.trimIndent()).enabled(false))

        // Remove Legend
        options.legend(AALegend().enabled(false))
        // yAxis options:
        // remove:
        // grid lines
        // Y-axis title
        options.yAxis(AAYAxis()).yAxis!!
            .gridLineWidth(0.00F)
            .allowDecimals(false)
            .visible(false)
            .title(AATitle().text(""))
        options.xAxis(AAXAxis()).xAxis!!.categories(Array(labels!!.size - 1) { i -> labels!![i] })
            .reversed(true)

        options.xAxis!!
            .lineWidth(0.00F)
            .gridLineWidth(0.00F)
            .tickWidth(0.1F)






        return this

    }

    fun getModel(): AAChartModel {
        return model
    }

    fun getOptions(): AAOptions {
        return options
    }


}