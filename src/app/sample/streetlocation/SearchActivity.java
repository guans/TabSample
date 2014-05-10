package app.sample.streetlocation;

import java.util.ArrayList;
import java.util.List;

import com.tencent.tencentmap.mapsdk.map.GeoPoint;
import com.tencent.tencentmap.mapsdk.map.PoiOverlay;
import com.tencent.tencentmap.mapsdk.search.PoiItem;
import com.tencent.tencentmap.mapsdk.search.PoiResults;
import com.tencent.tencentmap.mapsdk.search.PoiSearch;
import com.tencent.tencentmap.streetviewsdk.StreetViewListener;

import android.R.string;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.streetlocation.R;

/**
 * @author Adil Soomro
 *
 */
public class SearchActivity extends Activity  {
	private static final int VOICE_RECOGNITION_REQUEST_CODE = 1234;
	private ImageButton voice, searchButton;
	private ProgressDialog progress, locationDialog, searchDialog;  //进度条
	private EditText searchEditText;
	private LinearLayout catering, shopping, bank, traffic, stay, live,
	entertainment, pub, carService, government;
	private String choice = "";
	private List<PoiItem> suggestionList;  //显示联想词
	private String key="";
	private PoiSearch  poiSearch;         //poi搜索
	private PoiResults poiResult;         //poi搜索结果
	private Dialog dialog;
	private int lat, lon;
	private ListView listInfo;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_actvity);      
        
        //poi搜索参数设置
       // PoiSearch  poiSearch=new PoiSearch(this);
        //poiSearch.setPageCapacity(15);
        //poiSearch.setPageNumber(0);
        
       
       // 
        
        voice = (ImageButton) this.findViewById(R.id.ht);
		voice.setOnClickListener(new VoiceListener());
		searchEditText = (EditText) this.findViewById(R.id.et_search);
		searchButton = (ImageButton) this.findViewById(R.id.bt_search);
		searchButton.setOnClickListener(new SearchListener());
        
		catering = (LinearLayout) findViewById(R.id.search_catering);
		catering.setOnClickListener(new MyListener());
		shopping = (LinearLayout) findViewById(R.id.search_shopping);
		shopping.setOnClickListener(new MyListener());
		bank = (LinearLayout) findViewById(R.id.search_bank);
		bank.setOnClickListener(new MyListener());
		traffic = (LinearLayout) findViewById(R.id.search_traffic);
		traffic.setOnClickListener(new MyListener());
		stay = (LinearLayout) findViewById(R.id.search_stay);
		stay.setOnClickListener(new MyListener());
		live = (LinearLayout) findViewById(R.id.search_life);
		live.setOnClickListener(new MyListener());
		entertainment = (LinearLayout) findViewById(R.id.search_entertainment);
		entertainment.setOnClickListener(new MyListener());
		pub = (LinearLayout) findViewById(R.id.search_pub);
		pub.setOnClickListener(new MyListener());
		carService = (LinearLayout) findViewById(R.id.search_car_service);
		carService.setOnClickListener(new MyListener());
		government = (LinearLayout) findViewById(R.id.search_government);
		government.setOnClickListener(new MyListener());
		
    }
    
    
    //语音识别功能
    class VoiceListener implements OnClickListener {

		public void onClick(View v) {
			// TODO Auto-generated method stub
			try {
				// 通过Intent传递语音识别的模式，开启语音
				Intent intent = new Intent(
						RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
				// 语言模式和自由模式的语音识别
				intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
						RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
				// 提示语音开始
				intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "开始语音");
				// 开始语音识别
				startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Toast.makeText(getApplicationContext(), "找不到语音设备", 1).show();
			}
		}
	}
    
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		// 回调获取从谷歌得到的数据
		if (requestCode == VOICE_RECOGNITION_REQUEST_CODE
				&& resultCode == RESULT_OK) {
			// 取得语音的字符
			ArrayList<String> results = data
					.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

			String resultString = "";
			for (int i = 0; i < results.size(); i++) {
				resultString += results.get(i);
			}
			Toast.makeText(this, resultString, 1).show();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
    
    
    
    class SearchListener implements OnClickListener {

		public void onClick(View v) {
			// TODO Auto-generated method stub
			String et = searchEditText.getText().toString().trim();
			if (!et.equals("")) {
				progress = ProgressDialog.show(SearchActivity.this, "搜索",
						"正在搜索,请稍等...", true, true);
				progress.setCanceledOnTouchOutside(false);
				progress.setOnCancelListener(new DialogInterface.OnCancelListener() {

					public void onCancel(DialogInterface dialog) {
						// TODO Auto-generated method stub
						Toast.makeText(SearchActivity.this, "搜索失败,请检查您的网络",
								Toast.LENGTH_LONG).show();
					}
				});
							
				
				 new Thread(new Runnable(){
			            @Override
			            public void run() {
			            	
			            	PoiSearch  poiSearch=new PoiSearch(SearchActivity.this);
							poiSearch.setPageCapacity(15);
							poiSearch.setPageNumber(0);
							
							PoiResults poiResult = new PoiResults();
							try {
								key=searchEditText.getText().toString().trim();
								if(key!="")
								{
								poiResult = poiSearch.searchPoiInCity(key,"武汉");
								}
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								
							}
							
							suggestionList=poiResult.getCurrentPagePoiItems();
							handler.sendEmptyMessage(0x0001);
			            }
			            
			        }).start();
				
			} else {
				Toast.makeText(SearchActivity.this, "请输入关键字", 1).show();
			}
		}

	}
    
    
    

		

		public void onGetSuggestionResult(final PoiResults result,
				int error) {
			// TODO Auto-generated method stub
			if (result == null) {
				return;
			}
			suggestionList = result.getCurrentPagePoiItems();
			handler.sendEmptyMessage(0x0001);
		}

		

	
    
    
    
    
    //进行UI更新的主进程
    private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 0x0001) {
				progress.dismiss();
				dialog = suggestionInfoDialog("您的搜索");
				WindowManager.LayoutParams params = dialog.getWindow()
						.getAttributes();
				params.alpha = 0.6f;
				dialog.getWindow().setAttributes(params);
				dialog.show();
			} else if (msg.what == 0x0002) {
				dialog.dismiss();
				locationDialog = ProgressDialog.show(SearchActivity.this, "定位",
						"正在定位,请稍等...", true, true);
				locationDialog.setCanceledOnTouchOutside(false);
				locationDialog.setIcon(getResources().getDrawable(
						R.drawable.place_icon));
				locationDialog
						.setOnCancelListener(new DialogInterface.OnCancelListener() {

							public void onCancel(DialogInterface dialog) {
								// TODO Auto-generated method stub
								Toast.makeText(SearchActivity.this,
										"定位失败,请检查您的网络", Toast.LENGTH_LONG)
										.show();
							}
						});
			} else if (msg.what == 0x0012) {    
				
				//locationDialog.dismiss();
				Intent intent = new Intent();
				intent.setClass(SearchActivity.this,
						HomeActivity.class);
				intent.putExtra("nlat", lat);
				intent.putExtra("nlon", lon);
				intent.putExtra("flag", "搜索");
				startActivity(intent);
			//	finish();
			}else if(msg.what == 0x0013){
				
				
				dialog = suggestionInfoDialog("您的搜索");
				WindowManager.LayoutParams params = dialog.getWindow()
						.getAttributes();
				params.alpha = 0.6f;
				dialog.getWindow().setAttributes(params);
				dialog.show();
				
			}
			else if(msg.what == 0x0014){
				
				searchEditText.setText(key);
				
			}
		};
	};
    
    
    
    
	private Dialog suggestionInfoDialog(String style) {
		LayoutInflater inflater = getLayoutInflater();
		View entryView = inflater.inflate(R.layout.list_info, null);
		AlertDialog.Builder builder = new AlertDialog.Builder(
				SearchActivity.this);
		builder.setIcon(getResources().getDrawable(R.drawable.place_icon));
		builder.setTitle(style);
		builder.setView(entryView);
		listInfo = (ListView) entryView.findViewById(R.id.list_info);
		if(suggestionList!=null)
		{
		listInfo.setAdapter(new MyBaseAdapter(SearchActivity.this,
				suggestionList));
		listInfo.setOnItemClickListener(new ListInfoListener());
		}
		else 
			Toast.makeText(SearchActivity.this, "搜索失败,请检查您的网络",
					Toast.LENGTH_LONG).show();		
		return builder.create();
	}
    
	

	// 点击LISTIFO内信息 响应
	class ListInfoListener implements OnItemClickListener {
		public void onItemClick(AdapterView<?> adapterView, View view,
				int position, long id) {
			
			System.out.println(suggestionList.get(position).name);
			lat=suggestionList.get(position).point.getLatitudeE6();
			lon=suggestionList.get(position).point.getLongitudeE6();
			handler.sendEmptyMessage(0x0012);
		}
	}
	
	
	
	//对LISTVIEW进行赋值
	class MyBaseAdapter extends BaseAdapter {
		private Context context; // 接收传入的环境
		private List<?> list; // 接收传入的List
		//@SuppressLint("ParserError")
		private LayoutInflater mInflater; // 引入布局资源的管理器

		//@SuppressLint("ParserError")
		public MyBaseAdapter(Context context, List<?> list) { // 构造函数
			this.context = context;
			mInflater = LayoutInflater.from(context);
			this.list = list;
		}

		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			TextView contentName = new TextView(context); // 构建一个TextView
			TextView contentContent = new TextView(context); // 构建一个TextView
			LinearLayout linearLayout = (LinearLayout) mInflater.inflate(
					R.layout.content, null); // 获取内容资源布局
			contentName.setTextSize(20);
			linearLayout.addView(contentName);
			linearLayout.addView(contentContent);
			contentName
					.setText(((List<PoiItem>) list).get(position).name); // 显示联想词
			return linearLayout;
		}
	}
	
	
	
 
    
    class MyListener implements OnClickListener {

		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.search_catering:
				cateringDialog();
				break;
			case R.id.search_shopping:
				shoppingDialog();
				break;
			case R.id.search_bank:
				bankDialog();
				break;
			case R.id.search_traffic:
				trafficDialog();
				break;
			case R.id.search_stay:
				stayDialog();
				break;
			case R.id.search_life:
				liveDialog();
				break;
			case R.id.search_entertainment:
				entertainmentDialog();
				break;
			case R.id.search_pub:
				pubFacilitiesDilog();
				break;
			case R.id.search_car_service:
				carServiceDialog();
				break;
			case R.id.search_government:
				governmentDialog();
				break;
			}
		}
	}
    


    
    
    private void cateringDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				SearchActivity.this);
		final  String[] str={ "餐厅", "中餐厅", "快餐厅", "咖啡厅",
				"蛋糕房", "肯德基", "麦当劳", "必胜客", "自助餐" };
		final int temp;
		builder.setTitle("查询");
		builder.setSingleChoiceItems(str , 0,
				new DialogInterface.OnClickListener() {
			
			       @Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub		
						switch(which)
						{
						case 0: choice = "餐厅"; break;
						case 1: choice = "中餐厅"; break;
						case 2: choice = "快餐厅"; break;
						case 3: choice = "咖啡厅"; break;
						case 4: choice = "蛋糕店"; break;
						case 5: choice = "肯德基"; break;
						case 6: choice = "麦当劳"; break;
						case 7: choice = "必胜客"; break;
						case 8: choice = "自助餐"; break;
						default:break;
						
						}	
						key=choice;	
						System.out.println(key);
					//	searchEditText.setText(key);
						handler.sendEmptyMessage(0x0014);
					
					}
				});
		builder.setPositiveButton("搜索", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
				dialog.dismiss();
				progress = ProgressDialog.show(SearchActivity.this, "搜索",
						"正在搜索,请稍等...", true, true);
				progress.setCanceledOnTouchOutside(false);
				
				progress.setOnCancelListener(new DialogInterface.OnCancelListener() {

					public void onCancel(DialogInterface dialog) {
						// TODO Auto-generated method stub
						Toast.makeText(SearchActivity.this, "搜索失败,请检查您的网络",
								Toast.LENGTH_LONG).show();
					}
				});
				
				new Thread(new Runnable(){
		            @Override
		            public void run() {
		            	
		            	PoiSearch  poiSearch=new PoiSearch(SearchActivity.this);
						poiSearch.setPageCapacity(15);
						poiSearch.setPageNumber(0);
						
						PoiResults poiResult = new PoiResults();
						try {
							key=searchEditText.getText().toString().trim();
							if(key!="")
							{
							poiResult = poiSearch.searchPoiInCity(key,"武汉");
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						suggestionList=poiResult.getCurrentPagePoiItems();
						handler.sendEmptyMessage(0x0001);
		            }
		            
		        }).start();
				
				
			}
		});
		
		
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		builder.create().show();
	}

	private void shoppingDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				SearchActivity.this);
		builder.setTitle("查询");
		builder.setSingleChoiceItems(new String[] { "商场", "超市", "书店", "市场" },
				0, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						if (which == 0) {
							choice = "商场";
						} else if (which == 1) {
							choice = "超市";
						} else if (which == 2) {
							choice = "书店";
						} else if (which == 3) {
							choice = "市场";
						}
						
						key=choice;	
						System.out.println(key);
//						searchEditText.setText(key);
						handler.sendEmptyMessage(0x0014);
					}
				});
		builder.setPositiveButton("搜索", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				progress = ProgressDialog.show(SearchActivity.this, "搜索",
						"正在搜索,请稍等...", true, true);
				progress.setCanceledOnTouchOutside(false);
				progress.setOnCancelListener(new DialogInterface.OnCancelListener() {

					public void onCancel(DialogInterface dialog) {
						// TODO Auto-generated method stub
						Toast.makeText(SearchActivity.this, "搜索失败,请检查您的网络",
								Toast.LENGTH_LONG).show();
					}
				});


				
				new Thread(new Runnable(){
		            @Override
		            public void run() {
		            	
		            	PoiSearch  poiSearch=new PoiSearch(SearchActivity.this);
						poiSearch.setPageCapacity(15);
						poiSearch.setPageNumber(0);
						
						PoiResults poiResult = new PoiResults();
						try {
							key=searchEditText.getText().toString().trim();
							if(key!="")
							{
							poiResult = poiSearch.searchPoiInCity(key,"武汉");
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						suggestionList=poiResult.getCurrentPagePoiItems();
						handler.sendEmptyMessage(0x0001);
		            }
		            
		        }).start();
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		builder.create().show();
	}

	private void bankDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				SearchActivity.this);
		builder.setTitle("查询");
		builder.setSingleChoiceItems(new String[] { "银行", "ATM", "招商银行",
				"工商银行", "中国银行", "建设银行", "农业银行", "交通银行", "邮政储蓄" }, 0,
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						if (which == 0) {
							choice = "银行";
						} else if (which == 1) {
							choice = "ATM";
						} else if (which == 2) {
							choice = "招商银行";
						} else if (which == 3) {
							choice = "工商银行";
						} else if (which == 4) {
							choice = "中国银行";
						} else if (which == 5) {
							choice = "建设银行";
						} else if (which == 6) {
							choice = "农业银行";
						} else if (which == 7) {
							choice = "交通银行";
						} else if (which == 8) {
							choice = "邮政储蓄";
						}
						
						key=choice;	
						System.out.println(key);
//						searchEditText.setText(key);
						handler.sendEmptyMessage(0x0014);
					}
				});
		builder.setPositiveButton("搜索", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				progress = ProgressDialog.show(SearchActivity.this, "搜索",
						"正在搜索,请稍等...", true, true);
				progress.setCanceledOnTouchOutside(false);
				progress.setOnCancelListener(new DialogInterface.OnCancelListener() {

					public void onCancel(DialogInterface dialog) {
						// TODO Auto-generated method stub
						Toast.makeText(SearchActivity.this, "搜索失败,请检查您的网络",
								Toast.LENGTH_LONG).show();
					}
				});


				new Thread(new Runnable(){
		            @Override
		            public void run() {
		            	
		            	PoiSearch  poiSearch=new PoiSearch(SearchActivity.this);
						poiSearch.setPageCapacity(15);
						poiSearch.setPageNumber(0);
						
						PoiResults poiResult = new PoiResults();
						try {
							key=searchEditText.getText().toString().trim();
							if(key!="")
							{
							poiResult = poiSearch.searchPoiInCity(key,"武汉");
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						suggestionList=poiResult.getCurrentPagePoiItems();
						handler.sendEmptyMessage(0x0001);
		            }
		            
		        }).start();
				
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		builder.create().show();
	}

	private void trafficDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				SearchActivity.this);
		builder.setTitle("查询");
		builder.setSingleChoiceItems(new String[] { "机场", "火车站", "公交站", "停车场",
				"轻轨站", "地铁站", "长途汽车站", "火车票代售点", "飞机票代售点" }, 0,
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						if (which == 0) {
							choice = "机场";
						} else if (which == 1) {
							choice = "火车站";
						} else if (which == 2) {
							choice = "公交站";
						} else if (which == 3) {
							choice = "停车场";
						} else if (which == 4) {
							choice = "轻轨站";
						} else if (which == 5) {
							choice = "地铁站";
						} else if (which == 6) {
							choice = "长途汽车站";
						} else if (which == 7) {
							choice = "火车票代售点";
						} else if (which == 8) {
							choice = "飞机票代售点";
						}
						key=choice;	
						System.out.println(key);
//						searchEditText.setText(key);
						handler.sendEmptyMessage(0x0014);
					}
				});
		builder.setPositiveButton("搜索", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				progress = ProgressDialog.show(SearchActivity.this, "搜索",
						"正在搜索,请稍等...", true, true);
				progress.setCanceledOnTouchOutside(false);
				progress.setOnCancelListener(new DialogInterface.OnCancelListener() {

					public void onCancel(DialogInterface dialog) {
						// TODO Auto-generated method stub
						Toast.makeText(SearchActivity.this, "搜索失败,请检查您的网络",
								Toast.LENGTH_LONG).show();
					}
				});


				new Thread(new Runnable(){
		            @Override
		            public void run() {
		            	
		            	PoiSearch  poiSearch=new PoiSearch(SearchActivity.this);
						poiSearch.setPageCapacity(15);
						poiSearch.setPageNumber(0);
						
						PoiResults poiResult = new PoiResults();
						try {
							key=searchEditText.getText().toString().trim();
							if(key!="")
							{
							poiResult = poiSearch.searchPoiInCity(key,"武汉");
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						suggestionList=poiResult.getCurrentPagePoiItems();
						handler.sendEmptyMessage(0x0001);
		            }
		            
		        }).start();
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		builder.create().show();
	}

	private void stayDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				SearchActivity.this);
		builder.setTitle("查询");
		builder.setSingleChoiceItems(new String[] { "饭店", "酒店", "宾馆", "旅馆" },
				0, new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						if (which == 0) {
							choice = "饭店";
						} else if (which == 1) {
							choice = "酒店";
						} else if (which == 2) {
							choice = "宾馆";
						} else if (which == 3) {
							choice = "旅馆";
						}
						key=choice;	
						System.out.println(key);
//						searchEditText.setText(key);
						handler.sendEmptyMessage(0x0014);
					}
				});
		builder.setPositiveButton("搜索", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				progress = ProgressDialog.show(SearchActivity.this, "搜索",
						"正在搜索,请稍等...", true, true);
				progress.setCanceledOnTouchOutside(false);
				progress.setOnCancelListener(new DialogInterface.OnCancelListener() {

					public void onCancel(DialogInterface dialog) {
						// TODO Auto-generated method stub
						Toast.makeText(SearchActivity.this, "搜索失败,请检查您的网络",
								Toast.LENGTH_LONG).show();
					}
				});


				new Thread(new Runnable(){
		            @Override
		            public void run() {
		            	
		            	PoiSearch  poiSearch=new PoiSearch(SearchActivity.this);
						poiSearch.setPageCapacity(15);
						poiSearch.setPageNumber(0);
						
						PoiResults poiResult = new PoiResults();
						try {
							key=searchEditText.getText().toString().trim();
							if(key!="")
							{
							poiResult = poiSearch.searchPoiInCity(key,"武汉");
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						suggestionList=poiResult.getCurrentPagePoiItems();
						handler.sendEmptyMessage(0x0001);
		            }
		            
		        }).start();
				
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		builder.create().show();
	}

	private void liveDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				SearchActivity.this);
		builder.setTitle("查询");
		builder.setSingleChoiceItems(new String[] { "学校", "邮局", "药店", "医院",
				"图书馆", "移动营业厅", "联通营业厅", "电信营业厅", "美容美发", "摄影冲印" }, 0,
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						if (which == 0) {
							choice = "学校";
						} else if (which == 1) {
							choice = "邮局";
						} else if (which == 2) {
							choice = "药店";
						} else if (which == 3) {
							choice = "医院";
						} else if (which == 4) {
							choice = "图书馆";
						} else if (which == 5) {
							choice = "移动营业厅";
						} else if (which == 6) {
							choice = "联通营业厅";
						} else if (which == 7) {
							choice = "电信营业厅";
						} else if (which == 8) {
							choice = "美容美发";
						} else if (which == 9) {
							choice = "摄影冲印";
						}
						key=choice;	
						System.out.println(key);
//						searchEditText.setText(key);
						handler.sendEmptyMessage(0x0014);
					}
				});
		builder.setPositiveButton("搜索", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				progress = ProgressDialog.show(SearchActivity.this, "搜索",
						"正在搜索,请稍等...", true, true);
				progress.setCanceledOnTouchOutside(false);
				progress.setOnCancelListener(new DialogInterface.OnCancelListener() {

					public void onCancel(DialogInterface dialog) {
						// TODO Auto-generated method stub
						Toast.makeText(SearchActivity.this, "搜索失败,请检查您的网络",
								Toast.LENGTH_LONG).show();
					}
				});


				new Thread(new Runnable(){
		            @Override
		            public void run() {
		            	
		            	PoiSearch  poiSearch=new PoiSearch(SearchActivity.this);
						poiSearch.setPageCapacity(15);
						poiSearch.setPageNumber(0);
						
						PoiResults poiResult = new PoiResults();
						try {
							key=searchEditText.getText().toString().trim();
							if(key!="")
							{
							poiResult = poiSearch.searchPoiInCity(key,"武汉");
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						suggestionList=poiResult.getCurrentPagePoiItems();
						handler.sendEmptyMessage(0x0001);
		            }
		            
		        }).start();
				
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		builder.create().show();
	}

	private void entertainmentDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				SearchActivity.this);
		builder.setTitle("查询");
		builder.setSingleChoiceItems(new String[] { "电影院", "KTV", "洗浴", "网吧",
				"台球厅", "酒吧", "茶馆", "景点" }, 0,
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						if (which == 0) {
							choice = "电影院";
						} else if (which == 1) {
							choice = "KTV";
						} else if (which == 2) {
							choice = "洗浴";
						} else if (which == 3) {
							choice = "网吧";
						} else if (which == 4) {
							choice = "台球厅";
						} else if (which == 5) {
							choice = "酒吧";
						} else if (which == 6) {
							choice = "茶馆";
						} else if (which == 7) {
							choice = "景点";
						}
						key=choice;	
						System.out.println(key);
//						searchEditText.setText(key);
						handler.sendEmptyMessage(0x0014);
					}
				});
		builder.setPositiveButton("搜索", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				progress = ProgressDialog.show(SearchActivity.this, "搜索",
						"正在搜索,请稍等...", true, true);
				progress.setCanceledOnTouchOutside(false);
				progress.setOnCancelListener(new DialogInterface.OnCancelListener() {

					public void onCancel(DialogInterface dialog) {
						// TODO Auto-generated method stub
						Toast.makeText(SearchActivity.this, "搜索失败,请检查您的网络",
								Toast.LENGTH_LONG).show();
					}
				});

				new Thread(new Runnable(){
		            @Override
		            public void run() {
		            	
		            	PoiSearch  poiSearch=new PoiSearch(SearchActivity.this);
						poiSearch.setPageCapacity(15);
						poiSearch.setPageNumber(0);
						
						PoiResults poiResult = new PoiResults();
						try {
							key=searchEditText.getText().toString().trim();
							if(key!="")
							{
							poiResult = poiSearch.searchPoiInCity(key,"武汉");
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						suggestionList=poiResult.getCurrentPagePoiItems();
						handler.sendEmptyMessage(0x0001);
		            }
		            
		        }).start();
				
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		builder.create().show();
	}

	private void pubFacilitiesDilog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				SearchActivity.this);
		builder.setTitle("查询");
		builder.setSingleChoiceItems(new String[] { "公厕", "公园", "公交站", "轻轨站",
				"地铁站" }, 0, new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				if (which == 0) {
					choice = "公厕";
				} else if (which == 1) {
					choice = "公园";
				} else if (which == 2) {
					choice = "公交站";
				} else if (which == 3) {
					choice = "轻轨站";
				} else if (which == 4) {
					choice = "地铁站";
				}
				key=choice;	
				System.out.println(key);
//				searchEditText.setText(key);
				handler.sendEmptyMessage(0x0014);
			}
		});
		builder.setPositiveButton("搜索", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				progress = ProgressDialog.show(SearchActivity.this, "搜索",
						"正在搜索,请稍等...", true, true);
				progress.setCanceledOnTouchOutside(false);
				progress.setOnCancelListener(new DialogInterface.OnCancelListener() {

					public void onCancel(DialogInterface dialog) {
						// TODO Auto-generated method stub
						Toast.makeText(SearchActivity.this, "搜索失败,请检查您的网络",
								Toast.LENGTH_LONG).show();
					}
				});


				new Thread(new Runnable(){
		            @Override
		            public void run() {
		            	
		            	PoiSearch  poiSearch=new PoiSearch(SearchActivity.this);
						poiSearch.setPageCapacity(15);
						poiSearch.setPageNumber(0);
						
						PoiResults poiResult = new PoiResults();
						try {
							key=searchEditText.getText().toString().trim();
							if(key!="")
							{
							poiResult = poiSearch.searchPoiInCity(key,"武汉");
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						suggestionList=poiResult.getCurrentPagePoiItems();
						handler.sendEmptyMessage(0x0001);
		            }
		            
		        }).start();
				
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		builder.create().show();
	}

	private void carServiceDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				SearchActivity.this);
		builder.setTitle("查询");
		builder.setSingleChoiceItems(new String[] { "加油站", "停车场", "汽车维修",
				"汽车服务" }, 0, new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				if (which == 0) {
					choice = "加油站";
				} else if (which == 1) {
					choice = "停车场";
				} else if (which == 2) {
					choice = "汽车维修";
				} else if (which == 3) {
					choice = "汽车服务";
				}
				key=choice;	
				System.out.println(key);
//				searchEditText.setText(key);
				handler.sendEmptyMessage(0x0014);
			}
		});
		builder.setPositiveButton("搜索", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				progress = ProgressDialog.show(SearchActivity.this, "搜索",
						"正在搜索,请稍等...", true, true);
				progress.setCanceledOnTouchOutside(false);
				progress.setOnCancelListener(new DialogInterface.OnCancelListener() {

					public void onCancel(DialogInterface dialog) {
						// TODO Auto-generated method stub
						Toast.makeText(SearchActivity.this, "搜索失败,请检查您的网络",
								Toast.LENGTH_LONG).show();
					}
				});


				new Thread(new Runnable(){
		            @Override
		            public void run() {
		            	
		            	PoiSearch  poiSearch=new PoiSearch(SearchActivity.this);
						poiSearch.setPageCapacity(15);
						poiSearch.setPageNumber(0);
						
						PoiResults poiResult = new PoiResults();
						try {
							key=searchEditText.getText().toString().trim();
							if(key!="")
							{
							poiResult = poiSearch.searchPoiInCity(key,"武汉");
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						suggestionList=poiResult.getCurrentPagePoiItems();
						handler.sendEmptyMessage(0x0001);
		            }
		            
		        }).start();
				
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		builder.create().show();
	}
	
	
	private void governmentDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				SearchActivity.this);
		builder.setTitle("查询");
		builder.setSingleChoiceItems(new String[] { "市政府", "公安局", "消防局", "街道办",
				"法院" }, 0, new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				if (which == 0) {
					choice = "市政府";
				} else if (which == 1) {
					choice = "公安局";
				} else if (which == 2) {
					choice = "消防局";
				} else if (which == 3) {
					choice = "街道办";
				} else if (which == 4) {
					choice = "法院";
				}
				key=choice;	
				System.out.println(key);
//				searchEditText.setText(key);
				handler.sendEmptyMessage(0x0014);
			}
		});
		builder.setPositiveButton("搜索", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				progress = ProgressDialog.show(SearchActivity.this, "搜索",
						"正在搜索,请稍等...", true, true);
				progress.setCanceledOnTouchOutside(false);
				progress.setOnCancelListener(new DialogInterface.OnCancelListener() {

					public void onCancel(DialogInterface dialog) {
						// TODO Auto-generated method stub
						Toast.makeText(SearchActivity.this, "搜索失败,请检查您的网络",
								Toast.LENGTH_LONG).show();
					}
				});


				new Thread(new Runnable(){
		            @Override
		            public void run() {
		            	
		            	PoiSearch  poiSearch=new PoiSearch(SearchActivity.this);
						poiSearch.setPageCapacity(15);
						poiSearch.setPageNumber(0);
						
						PoiResults poiResult = new PoiResults();
						try {
							key=searchEditText.getText().toString().trim();
							if(key!="")
							{
							poiResult = poiSearch.searchPoiInCity(key,"武汉");
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						suggestionList=poiResult.getCurrentPagePoiItems();
						handler.sendEmptyMessage(0x0001);
		            }
		            
		        }).start();
				
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		builder.create().show();
	}
	
	
    
}
