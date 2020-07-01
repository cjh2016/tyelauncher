/**
 * 章节图谱显示逻辑
 * by jjchen
 */
(function () {
    Array.prototype.contains = function (obj) {
      var i = this.length
      while (i--) {
        if (this[i] === obj) {
          return true
        }
      }
      return false
    }

    /**
     * 锚点颜色表，对应掌握程度，使用rgb颜色，方便获取颜色值。
     * @type {{unknow: string, high: string, mid: string, low: string}}
     * @private
     */
    var _cateColor = {
      //掌握情况未知的情况
      'unknow': 'rgb(153,153,153)',
      //掌握较好
      'high': 'rgb(13,194,179)',
      //掌握一般
      'mid': 'rgb(255,176,51)',
      //掌握较差
      'low': 'rgb(255,98,87)'
    }

    /**
     * 根据掌握程度获取颜色值
     * @param mastery 掌握程度
     * @returns {string}
     */
    function getCateColor (mastery) {
      if (typeof mastery === 'number') {
        if (mastery < 0.35) {
          return _cateColor.low
        }
        else if (mastery >= 0.65) {
          return _cateColor.high
        }
        else {
          return _cateColor.mid
        }
      }

      return _cateColor.unknow
    }

    /**
     * 获取锚点开关，当锚点有真实掌握程度时，显示圆形，否则显示菱形。
     * @param real 是否有真实掌握程度
     * @returns {string}
     */
    function getBallStyle (real) {
      return real ? 'circle' : 'diamond'
    }

    /**
     * 获取推荐展示层
     * @param data
     * @returns {}
     */
    function getRecommendSeries (data) {
      return {
        type: 'effectScatter',
        showEffectOn: 'render',
        coordinateSystem: 'cartesian2d',
        rippleEffect: {
          brushType: 'stroke',
          period: 3,
          scale: 3
        },
        data: data,
        symbolSize: 30,
        z: 7
      }
    }

    /**
     * 获取选中效果展示层
     * @param data
     * @returns {}
     */
    function getSelectedSeries (data) {
      return {
        type: 'scatter',
        showEffectOn: 'render',
        coordinateSystem: 'cartesian2d',
        data: data,
        symbolSize: 30,
        z: 8,
        itemStyle: {
          color: 'rgba(0, 0, 0, 0)',
          borderColor: {
            type: 'linear',
            x: 0,
            y: 0,
            x2: 0,
            y2: 1,
            colorStops: [{
              offset: 0.3, color: '#000080' // 0% 处的颜色
            }, {
              offset: 0.5, color: '#0000EE'
            }, {
              offset: 1, color: '#00868B' // 100% 处的颜色
            }
            ],
            globalCoord: false // 缺省为 false
          },
          borderWidth: 3
        }
      }
    }

    /**
     * 获取图谱展示层
     * @param data
     * @param linkData
     * @param hoverAnimation
     * @returns {}
     */
    function getGraphSeries (data, linkData, hoverAnimation) {
      return {
        type: 'graph',
        layout: 'none',
        coordinateSystem: 'cartesian2d',
        edgeSymbol: ['none', 'arrow'],
        edgeSymbolSize: [4, 6],
        data: data,
        selectedMode: 'multiple',
        hoverAnimation: hoverAnimation,
        links: linkData,
        z: 9,
        label: {
          normal: {
            show: true,
            position: 'top',
            formatter: function (params) {
              if (data.length > 24 && !params.data.showLabel) {
                return ''
              }
              var _name = params.name
              var LINE_CHAR = 7, _val = '', _i = 1
              if (params.data.selected) {
                var _lines = Math.ceil(_name.length / LINE_CHAR)
                _val += _name.substr(0, LINE_CHAR)
                while (_i < _lines) {
                  _val += '\n' + _name.substr(_i * LINE_CHAR, LINE_CHAR)
                  _i++
                }
                return _val
              } else {
                var _lines = Math.ceil(_name.length / LINE_CHAR) - 1
                _val += _name.substr(0, LINE_CHAR)
                if (_name.length > LINE_CHAR) {
                  _val += '\n' + _name.substr(LINE_CHAR, LINE_CHAR)
                  if (_name.length > LINE_CHAR * 2) {
                    _val += '...'
                  }
                }
                return _val
              }
            }
          },
          emphasis: {
            position: 'top',
            show: true
          }
        },
        roam: false,
        focusNodeAdjacency: false,
        lineStyle: {
          color: 'target'
        }
      }
    }

    /**
     * 获取提示展示层
     * @param data
     * @returns {}
     */
    function getTipSeries (data) {
      return {
        type: 'graph',
        layout: 'none',
        coordinateSystem: 'cartesian2d',
        edgeSymbol: ['none', 'arrow'],
        edgeSymbolSize: [4, 6],
        data: data,
        selectedMode: 'multiple',
        z: 10
      }
    }

    /**
     * 获取推荐展示数据
     * @param nodes
     * @returns {Array}
     */
    function generateGraphData (nodes, sid) {
      var data = []
      nodes.forEach(function (node) {
        data.push({
          anchorId: node.id,
          name: node.name,
          topicId: node.topicId,
          value: [node.x, node.y],
          itemStyle: {
            color: node.color
          },
          symbol: node.symbol,
          symbolSize: node.symbolSize,
          selected: node.id == sid || node.postAnchors.contains(sid) || node.preAnchors.contains(sid),
          showLabel: node.showLabel || node.id == sid || node.postAnchors.contains(sid) || node.preAnchors.contains(sid),
          realMastery: node.realMastery
        })
      })
      return data
    }

    /**
     * 获取提示展示数据
     * @param nodes
     * @param baseData
     * @returns {Array}
     */
    function generateTipData (nodes, baseData) {
      var data = []
      nodes.forEach(function (node) {
        var isMaxY = baseData.maxY - node.y < baseData.maxY / 6

        data.push({
          name: node.name,
          value: [node.x, node.y],
          symbolSize: 0.1,
          symbol: node.symbol,
          itemStyle: {
            color: node.color
          },
          label: {
            normal: {
              show: true,
              position: isMaxY ? 'top' : 'bottom',
              distance: node.symbolSize / 2 + 8,
              formatter: function (params) {
                var _name = params.name
                var LINE_CHAR = 10, _val = '', _i = 0
                var _lines = Math.ceil(_name.length / LINE_CHAR) - 1
                while (_i < _lines) {
                  _val += _name.substr(_i * LINE_CHAR, LINE_CHAR) + '\n'
                  _i++
                }
                return '{head|最优学习考点：}\n' + '{title|' + _val + _name.substring(_i * LINE_CHAR) + '}'
              },
              backgroundColor: 'rgba(0,0,0,0.6)',
              borderRadius: 8,
              textBorderWidth: 0,
              padding: [16, 16],
              align: 'center',
              rich: {
                head: {
                  fontSize: 18,
                  lineHeight: 30,
                  color: '#fff',
                  align: 'left'
                },
                title: {
                  fontSize: 16,
                  lineHeight: 20,
                  color: '#fff',
                  align: 'left'
                }
              }
            }
          }
        })
      })
      return data
    }

    /**
     * 获取推荐展示数据
     * @param nodes
     * @param sid 选中的锚点id
     * @returns {Array}
     */
    function generateRecommendData (nodes, sid) {
      var data = []
      nodes.forEach(function (node) {
        if (sid) {
          if (node.id != sid && !node.postAnchors.contains(sid) && !node.preAnchors.contains(sid)) {
            return
          }
        }
        data.push({
          name: node.name,
          value: [node.x, node.y],
          symbolSize: node.symbolSize,
          itemStyle: {
            color: node.color,
            borderColor: 'rgba(255, 255, 255,1)',
            borderWidth: node.symbolSize / 4 + 2
          }
        })
      })
      return data
    }

    /**
     * 获取选择展示数据
     * @param nodes
     * @param name
     */
    function generateSelectedData (nodes, name) {
      var data = {name: name}
      var nodeId = ''
      for (var i = 0; i < nodes.length; i++) {
        if (nodes[i].name == name) {
          data.value = [nodes[i].x, nodes[i].y]
          data.symbolSize = nodes[i].symbolSize + 15
          break
        }
      }
      return [data]
    }

    /**
     * 生成节点信息列表
     * @param nodes
     * @param links
     * @param recommendNodes
     * @param anchors
     * @param masteries
     * @param recommendTopicId 推荐锚点ID
     */
    function generateNodes (nodes, links, recommendNodes, anchors, masteries, recommendTopicId) {
      //处理掌握程度
      var masteryMap = {}
      masteries.forEach(function (item) {
        masteryMap[item.anchorId] = {
          color: getCateColor(item.mastery),
          isReal: item.realMastery >= 0,
          mastery: item.mastery,
          realMastery: Math.round(item.realMastery * 100)
        }
      })

      //处理锚点
      var anchorMap = {}
      anchors.forEach(function (anchor) {
        anchorMap[anchor.anchorPointId] = anchor
      })

      //处理锚点关系
      anchors.forEach(function (anchor) {
        // 出
        anchor.postLength = 0
        if (anchor.postAnchors.length) {
          anchor.postAnchors.forEach(function (post) {
            if (anchorMap[post]) {
              links.push({
                source: anchor.anchorPointName,
                target: anchorMap[post].anchorPointName,
                lineStyle: {
                  color: '#0DC2B3'
                }
              })
              anchor.postLength++
            }
          })
        }
        // 入
        anchor.preLength = 0
        if (anchor.preAnchors.length) {
          anchor.preAnchors.forEach(function (prev) {
            if (anchorMap[prev]) {
              links.push({
                source: anchorMap[prev].anchorPointName,
                target: anchor.anchorPointName,
                lineStyle: {
                  color: '#0DC2B3'
                }
              })
              anchor.preLength++
            }
          })
        }
      })

      anchors.forEach(function (anchor) {
        var mastery = masteryMap[anchor.anchorTopicId]
        //取前驱或后置数量的最大值
        var length = Math.max(anchor.preAnchors.length, anchor.postAnchors.length)
        var node = {
          id: anchor.anchorPointId,
          topicId: anchor.anchorTopicId,
          name: anchor.anchorPointName,
          postAnchors: anchor.postAnchors,
          preAnchors: anchor.preAnchors,
          showLabel: anchor.preLength + anchor.postLength > 2 || (mastery && mastery.isReal),
          //根据前驱或后置数量，设置锚点大小，>3：1.2，2或3：1，<2：0.6
          symbolSize: 30 * (length > 3 ? 1.2 : (length > 1 ? 1 : 0.8)),
          realMastery: mastery ? mastery.realMastery : -1,
          x: anchor.anchorPointPosition.X,
          y: anchor.anchorPointPosition.Y,
          color: mastery ? getCateColor(mastery.mastery) : _cateColor.unknow,
          symbol: getBallStyle(mastery ? mastery.isReal : true)
        }

        nodes.push(node)

        if (node.topicId === recommendTopicId) {
          recommendNodes.push(node)
        }
      })
    }

    /**
     * 转换锚点坐标值
     * @param anchors 锚点列表
     * @param baseData 基础数据
     */
    function translatePosition (anchors, baseData) {
      var loose = false
      for (var i = 0; i < anchors.length; i++) {
        if (!anchors[i].anchorPointPosition) {
          loose = true
          break
        }
      }

      //对缺失的坐标进行计数处理
      var missCoordinatesCount = 0
      anchors.forEach(function (anchor) {   // 坐标转换
        if (loose) {
          if (!anchor.anchorPointPosition) //如果坐标缺少，则设为0
          {
            anchor.anchorPointPosition = {'X': missCoordinatesCount++, 'Y': 0}
          } else {
            anchor.anchorPointPosition.Y++
          }
        }

        var bgX = anchor.anchorPointPosition.X
        var bgY = anchor.anchorPointPosition.Y * 2 + ((bgX % 2) ? 0 : 1)
        anchor.anchorPointPosition.X = (0.5 + bgX)
        anchor.anchorPointPosition.Y = (1 + bgY) / 2

        if (anchor.anchorPointPosition.X > baseData.maxX) {
          baseData.maxX = anchor.anchorPointPosition.X
        }
        if (anchor.anchorPointPosition.X < baseData.minX) {
          baseData.minX = anchor.anchorPointPosition.X
        }

        if (anchor.anchorPointPosition.Y > baseData.maxY) {
          baseData.maxY = anchor.anchorPointPosition.Y
        }
        if (anchor.anchorPointPosition.Y < baseData.minY) {
          baseData.minY = anchor.anchorPointPosition.Y
        }
      })

      //点少时水平居中
      if (baseData.maxX - baseData.minX < 2) {
        baseData.maxX += 0.5
        baseData.minX -= 0.5
      }

      //点少时垂直居中
      if (baseData.maxY - baseData.minY < 2) {
        baseData.maxY += 0.5
        baseData.minY -= 0.5
      }

      baseData.maxX -= baseData.minX
      baseData.maxY -= baseData.minY
      anchors.forEach(function (anchor) {
        anchor.anchorPointPosition.X -= baseData.minX
        anchor.anchorPointPosition.Y -= baseData.minY
      })
    }

    /**
     * 调用android方法
     * @param id
     * @param name
     */
    function callAndroid (id, name) {
      document.location = 'js://clickAnchorPoint?id=' + id + '&name=' + name
    }

    /**
     * 图谱数据缓存
     * @type {}
     * @private
     */
    var _graphData = null

    /**
     * 显示新图谱画像全局方法
     * @param anchors 锚点列表
     * @param masteries 掌握程度列表
     * @param recommendTopicId 推荐锚点ID
     * @param allowOperate 是否允许操作
     * @param showAnimate 是否显示动画
     */
    window.showAnchorGraph = function (anchors, masteries, recommendTopicId, allowOperate, showAnimate, showPredict) {
      var baseData = {maxX: 0, maxY: 0, minX: Number.MAX_VALUE, minY: Number.MAX_VALUE}
      translatePosition(anchors, baseData)

      //不显示预测画像的情况下，删除画像中预测节点。
      if (!showPredict && masteries) {
        for (var i = masteries.length - 1; i >= 0; i--) {
          if (masteries[i].realMastery < 0) {
            masteries.splice(i, 1)
          }
        }
      }

      var nodes = [], links = [], recommendNodes = []
      generateNodes(nodes, links, recommendNodes, anchors, masteries, recommendTopicId)

      var selectedData = []
      var recommendData = generateRecommendData(recommendNodes)
      var tipData = generateTipData(recommendNodes, baseData)
      var graphData = generateGraphData(nodes)
      var linksData = links

      // echarts插件对象
      var myChart = echarts.init(document.getElementById('main'))
      myChart.clear()

      //清空并绑定点击事件
      var clicked = false
      myChart.off('mousedown')
      myChart.on('mousedown', function (params) {
        if (!allowOperate || params.dataType !== 'node' || params.seriesIndex != 2) {
          //移除优化点提示
          if (tipData.length > 0) {
            var series = myChart.getOption().series
            if (series.length == 4) {
              tipData = []
              showGraph()
            }
          }
          return
        }

        clicked = true
        //移除优化点提示
        if (tipData.length > 0) {
          tipData = []
        }
        selectedData = generateSelectedData(nodes, params.data.name)
        recommendData = generateRecommendData(recommendNodes, params.data.anchorId)
        graphData = generateGraphData(nodes, params.data.anchorId)
        showGraph()
        myChart.dispatchAction({
          type: 'focusNodeAdjacency',
          seriesIndex: params.seriesIndex,
          dataIndex: params.dataIndex
        })
        callAndroid(params.data.topicId, params.data.name)
      })

      //窗口点击事件
      window.onmousedown = function () {
        if (tipData.length > 0) {
          var series = myChart.getOption().series
          if (series.length == 4) {
            tipData = []
            showGraph()
          }
        }

        //在没有点击锚点的情况下判断并取消锚点选择状态
        if (clicked) {
          clicked = false
        } else if (selectedData.length) {
          selectedData = []
          recommendData = generateRecommendData(recommendNodes)
          graphData = generateGraphData(nodes)
          showGraph()
          callAndroid('mouseout')
        }
      }

      window.onresize = function () {
          myChart.resize()
      }

      function showGraph (animated) {
        //如果显示动画且上次数据存在，则显示动画
        if (animated && _graphData) {
          var source = _graphData
          var dest = graphData
          _graphData = graphData
          var interval = 60, count = 6
          var data = []
          for (var i = 0; i < count + 2; i++) {
            data[i] = i
          }
          var baseOption = {
            animation: false,
            timeline: {
              show: false,
              autoPlay: true,
              playInterval: interval,
              loop: false,
              data: data
            },
            grid: {
              top: 66,
              left: '10%',
              right: '10%',
              bottom: 60
            },
            xAxis: {
              type: 'value',
              show: false,
              // min: baseData.minX,
              max: baseData.maxX,
              position: 'top'
            },
            yAxis: {
              type: 'value',
              show: false,
              // min: baseData.minY,
              max: baseData.maxY,
              inverse: true
            },
            dataZoom: [
              {
                type: 'inside',
                xAxisIndex: [0],
                start: 0,
                end: 100,
                filterMode: 'none'
              },
              {
                type: 'inside',
                yAxisIndex: [0],
                start: 0,
                end: 100,
                filterMode: 'none'
              }
            ]
          }

          var options = []
          options[0] = {'series': [getGraphSeries(source, linksData, false)]}
          options[count + 1] = {'series': [getSelectedSeries([]), getRecommendSeries(recommendData), getGraphSeries(dest, linksData, allowOperate), getTipSeries(tipData)]}
          var rgbs = []
          for (var j = 0; j < source.length; j++) {
            //避免两次画像不一致的问题
            if (!dest[j]) {
              dest[j] = source[j]
            }
            var rgb = source[j].itemStyle.color.substr(4).split(',')
            var rs = parseInt(rgb[0])
            var gs = parseInt(rgb[1])
            var bs = parseInt(rgb[2])

            rgb = dest[j].itemStyle.color.substr(4).split(',')
            var rd = parseInt(rgb[0])
            var gd = parseInt(rgb[1])
            var bd = parseInt(rgb[2])

            var ri = Math.floor((rd - rs) / (count + 1))
            var gi = Math.floor((gd - gs) / (count + 1))
            var bi = Math.floor((bd - bs) / (count + 1))

            if (ri != 0 || gi != 0 || bi != 0) {
              rgbs[j] = []
              for (var i = 0; i < count; i++) {
                rs += ri
                gs += gi
                bs += bi
                rgbs[j][i] = 'rgb(' + rs + ',' + gs + ',' + bs + ')'
              }
            } else {
              rgbs[j] = null
            }
          }
          var rate = 1.5
          for (var i = 1; i <= count; i++) {
            var temp = []
            for (var j = 0; j < dest.length; j++) {
              temp[j] = {}
              for (var key in dest[j]) {
                temp[j][key] = dest[j][key]
              }
              if (source[j] && source[j].realMastery != dest[j].realMastery) {
                temp[j].symbolSize = Math.round(dest[j].symbolSize * (i * rate / count))
              }
              if (rgbs[j]) {
                temp[j].itemStyle = {'color': rgbs[j][i - 1]}
              }
            }
            options[i] = {'series': [getGraphSeries(temp, linksData, false)]}
          }
          myChart.setOption({
            baseOption: baseOption,
            options: options
          }, true)
        } else {
          _graphData = graphData
          var series = [
            getSelectedSeries(selectedData),
            getRecommendSeries(recommendData),
            getGraphSeries(graphData, linksData, allowOperate),
            getTipSeries(tipData)
          ]

          //记住上次缩放大小
          var startX = 0, endX = 100, startY = 0, endY = 100
          var option = myChart.getOption()
          if (option && option.dataZoom) {
            if (option.dataZoom[0]) {
              startX = option.dataZoom[0].start
              endX = option.dataZoom[0].end
            }
            if (option.dataZoom[1]) {
              startY = option.dataZoom[1].start
              endY = option.dataZoom[1].end
            }
          }

          myChart.setOption({
            animation: false,
            grid: {
              top: 66,
              left: '10%',
              right: '10%',
              bottom: 60
            },
            xAxis: {
              type: 'value',
              show: false,
              // min: baseData.minX,
              max: baseData.maxX,
              position: 'top'
            },
            yAxis: {
              type: 'value',
              show: false,
              // min: baseData.minY,
              max: baseData.maxY,
              inverse: true
            },
            dataZoom: [
              {
                type: 'inside',
                xAxisIndex: [0],
                start: startX,
                end: endX,
                filterMode: 'none'
              },
              {
                type: 'inside',
                yAxisIndex: [0],
                start: startY,
                end: endY,
                filterMode: 'none'
              }
            ],
            series: series
          }, true)
        }
      }

      showGraph(showAnimate)
    }

  }
)()
