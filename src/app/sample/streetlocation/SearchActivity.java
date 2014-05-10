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
	private ProgressDialog progress, locationDialog, searchDialog;  //������
	private EditText searchEditText;
	private LinearLayout catering, shopping, bank, traffic, stay, live,
	entertainment, pub, carService, government;
	private String choice = "";
	private List<PoiItem> suggestionList;  //��ʾ�����
	private String key="";
	private PoiSearch  poiSearch;         //poi����
	private PoiResults poiResult;         //poi�������
	private Dialog dialog;
	private int lat, lon;
	private ListView listInfo;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_actvity);      
        
        //poi������������
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
    
    
    //����ʶ����
    class VoiceListener implements OnClickListener {

		public void onClick(View v) {
			// TODO Auto-generated method stub
			try {
				// ͨ��Intent��������ʶ���ģʽ����������
				Intent intent = new Intent(
						RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
				// ����ģʽ������ģʽ������ʶ��
				intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
						RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
				// ��ʾ������ʼ
				intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "��ʼ����");
				// ��ʼ����ʶ��
				startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Toast.makeText(getApplicationContext(), "�Ҳ��������豸", 1).show();
			}
		}
	}
    
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		// �ص���ȡ�ӹȸ�õ�������
		if (requestCode == VOICE_RECOGNITION_REQUEST_CODE
				&& resultCode == RESULT_OK) {
			// ȡ���������ַ�
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
				progress = ProgressDialog.show(SearchActivity.this, "����",
						"��������,���Ե�...", true, true);
				progress.setCanceledOnTouchOutside(false);
				progress.setOnCancelListener(new DialogInterface.OnCancelListener() {

					public void onCancel(DialogInterface dialog) {
						// TODO Auto-generated method stub
						Toast.makeText(SearchActivity.this, "����ʧ��,������������",
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
								poiResult = poiSearch.searchPoiInCity(key,"�人");
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
				Toast.makeText(SearchActivity.this, "������ؼ���", 1).show();
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

		

	
    
    
    
    
    //����UI���µ�������
    private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 0x0001) {
				progress.dismiss();
				dialog = suggestionInfoDialog("��������");
				WindowManager.LayoutParams params = dialog.getWindow()
						.getAttributes();
				params.alpha = 0.6f;
				dialog.getWindow().setAttributes(params);
				dialog.show();
			} else if (msg.what == 0x0002) {
				dialog.dismiss();
				locationDialog = ProgressDialog.show(SearchActivity.this, "��λ",
						"���ڶ�λ,���Ե�...", true, true);
				locationDialog.setCanceledOnTouchOutside(false);
				locationDialog.setIcon(getResources().getDrawable(
						R.drawable.place_icon));
				locationDialog
						.setOnCancelListener(new DialogInterface.OnCancelListener() {

							public void onCancel(DialogInterface dialog) {
								// TODO Auto-generated method stub
								Toast.makeText(SearchActivity.this,
										"��λʧ��,������������", Toast.LENGTH_LONG)
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
				intent.putExtra("flag", "����");
				startActivity(intent);
			//	finish();
			}else if(msg.what == 0x0013){
				
				
				dialog = suggestionInfoDialog("��������");
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
			Toast.makeText(SearchActivity.this, "����ʧ��,������������",
					Toast.LENGTH_LONG).show();		
		return builder.create();
	}
    
	

	// ���LISTIFO����Ϣ ��Ӧ
	class ListInfoListener implements OnItemClickListener {
		public void onItemClick(AdapterView<?> adapterView, View view,
				int position, long id) {
			
			System.out.println(suggestionList.get(position).name);
			lat=suggestionList.get(position).point.getLatitudeE6();
			lon=suggestionList.get(position).point.getLongitudeE6();
			handler.sendEmptyMessage(0x0012);
		}
	}
	
	
	
	//��LISTVIEW���и�ֵ
	class MyBaseAdapter extends BaseAdapter {
		private Context context; // ���մ���Ļ���
		private List<?> list; // ���մ����List
		//@SuppressLint("ParserError")
		private LayoutInflater mInflater; // ���벼����Դ�Ĺ�����

		//@SuppressLint("ParserError")
		public MyBaseAdapter(Context context, List<?> list) { // ���캯��
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
			TextView contentName = new TextView(context); // ����һ��TextView
			TextView contentContent = new TextView(context); // ����һ��TextView
			LinearLayout linearLayout = (LinearLayout) mInflater.inflate(
					R.layout.content, null); // ��ȡ������Դ����
			contentName.setTextSize(20);
			linearLayout.addView(contentName);
			linearLayout.addView(contentContent);
			contentName
					.setText(((List<PoiItem>) list).get(position).name); // ��ʾ�����
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
		final  String[] str={ "����", "�в���", "�����", "������",
				"���ⷿ", "�ϵ»�", "����", "��ʤ��", "������" };
		final int temp;
		builder.setTitle("��ѯ");
		builder.setSingleChoiceItems(str , 0,
				new DialogInterface.OnClickListener() {
			
			       @Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub		
						switch(which)
						{
						case 0: choice = "����"; break;
						case 1: choice = "�в���"; break;
						case 2: choice = "�����"; break;
						case 3: choice = "������"; break;
						case 4: choice = "�����"; break;
						case 5: choice = "�ϵ»�"; break;
						case 6: choice = "����"; break;
						case 7: choice = "��ʤ��"; break;
						case 8: choice = "������"; break;
						default:break;
						
						}	
						key=choice;	
						System.out.println(key);
					//	searchEditText.setText(key);
						handler.sendEmptyMessage(0x0014);
					
					}
				});
		builder.setPositiveButton("����", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
				dialog.dismiss();
				progress = ProgressDialog.show(SearchActivity.this, "����",
						"��������,���Ե�...", true, true);
				progress.setCanceledOnTouchOutside(false);
				
				progress.setOnCancelListener(new DialogInterface.OnCancelListener() {

					public void onCancel(DialogInterface dialog) {
						// TODO Auto-generated method stub
						Toast.makeText(SearchActivity.this, "����ʧ��,������������",
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
							poiResult = poiSearch.searchPoiInCity(key,"�人");
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
		
		
		builder.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {

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
		builder.setTitle("��ѯ");
		builder.setSingleChoiceItems(new String[] { "�̳�", "����", "���", "�г�" },
				0, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						if (which == 0) {
							choice = "�̳�";
						} else if (which == 1) {
							choice = "����";
						} else if (which == 2) {
							choice = "���";
						} else if (which == 3) {
							choice = "�г�";
						}
						
						key=choice;	
						System.out.println(key);
//						searchEditText.setText(key);
						handler.sendEmptyMessage(0x0014);
					}
				});
		builder.setPositiveButton("����", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				progress = ProgressDialog.show(SearchActivity.this, "����",
						"��������,���Ե�...", true, true);
				progress.setCanceledOnTouchOutside(false);
				progress.setOnCancelListener(new DialogInterface.OnCancelListener() {

					public void onCancel(DialogInterface dialog) {
						// TODO Auto-generated method stub
						Toast.makeText(SearchActivity.this, "����ʧ��,������������",
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
							poiResult = poiSearch.searchPoiInCity(key,"�人");
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
		builder.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {

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
		builder.setTitle("��ѯ");
		builder.setSingleChoiceItems(new String[] { "����", "ATM", "��������",
				"��������", "�й�����", "��������", "ũҵ����", "��ͨ����", "��������" }, 0,
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						if (which == 0) {
							choice = "����";
						} else if (which == 1) {
							choice = "ATM";
						} else if (which == 2) {
							choice = "��������";
						} else if (which == 3) {
							choice = "��������";
						} else if (which == 4) {
							choice = "�й�����";
						} else if (which == 5) {
							choice = "��������";
						} else if (which == 6) {
							choice = "ũҵ����";
						} else if (which == 7) {
							choice = "��ͨ����";
						} else if (which == 8) {
							choice = "��������";
						}
						
						key=choice;	
						System.out.println(key);
//						searchEditText.setText(key);
						handler.sendEmptyMessage(0x0014);
					}
				});
		builder.setPositiveButton("����", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				progress = ProgressDialog.show(SearchActivity.this, "����",
						"��������,���Ե�...", true, true);
				progress.setCanceledOnTouchOutside(false);
				progress.setOnCancelListener(new DialogInterface.OnCancelListener() {

					public void onCancel(DialogInterface dialog) {
						// TODO Auto-generated method stub
						Toast.makeText(SearchActivity.this, "����ʧ��,������������",
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
							poiResult = poiSearch.searchPoiInCity(key,"�人");
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
		builder.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {

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
		builder.setTitle("��ѯ");
		builder.setSingleChoiceItems(new String[] { "����", "��վ", "����վ", "ͣ����",
				"���վ", "����վ", "��;����վ", "��Ʊ���۵�", "�ɻ�Ʊ���۵�" }, 0,
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						if (which == 0) {
							choice = "����";
						} else if (which == 1) {
							choice = "��վ";
						} else if (which == 2) {
							choice = "����վ";
						} else if (which == 3) {
							choice = "ͣ����";
						} else if (which == 4) {
							choice = "���վ";
						} else if (which == 5) {
							choice = "����վ";
						} else if (which == 6) {
							choice = "��;����վ";
						} else if (which == 7) {
							choice = "��Ʊ���۵�";
						} else if (which == 8) {
							choice = "�ɻ�Ʊ���۵�";
						}
						key=choice;	
						System.out.println(key);
//						searchEditText.setText(key);
						handler.sendEmptyMessage(0x0014);
					}
				});
		builder.setPositiveButton("����", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				progress = ProgressDialog.show(SearchActivity.this, "����",
						"��������,���Ե�...", true, true);
				progress.setCanceledOnTouchOutside(false);
				progress.setOnCancelListener(new DialogInterface.OnCancelListener() {

					public void onCancel(DialogInterface dialog) {
						// TODO Auto-generated method stub
						Toast.makeText(SearchActivity.this, "����ʧ��,������������",
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
							poiResult = poiSearch.searchPoiInCity(key,"�人");
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
		builder.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {

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
		builder.setTitle("��ѯ");
		builder.setSingleChoiceItems(new String[] { "����", "�Ƶ�", "����", "�ù�" },
				0, new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						if (which == 0) {
							choice = "����";
						} else if (which == 1) {
							choice = "�Ƶ�";
						} else if (which == 2) {
							choice = "����";
						} else if (which == 3) {
							choice = "�ù�";
						}
						key=choice;	
						System.out.println(key);
//						searchEditText.setText(key);
						handler.sendEmptyMessage(0x0014);
					}
				});
		builder.setPositiveButton("����", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				progress = ProgressDialog.show(SearchActivity.this, "����",
						"��������,���Ե�...", true, true);
				progress.setCanceledOnTouchOutside(false);
				progress.setOnCancelListener(new DialogInterface.OnCancelListener() {

					public void onCancel(DialogInterface dialog) {
						// TODO Auto-generated method stub
						Toast.makeText(SearchActivity.this, "����ʧ��,������������",
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
							poiResult = poiSearch.searchPoiInCity(key,"�人");
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
		builder.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {

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
		builder.setTitle("��ѯ");
		builder.setSingleChoiceItems(new String[] { "ѧУ", "�ʾ�", "ҩ��", "ҽԺ",
				"ͼ���", "�ƶ�Ӫҵ��", "��ͨӪҵ��", "����Ӫҵ��", "��������", "��Ӱ��ӡ" }, 0,
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						if (which == 0) {
							choice = "ѧУ";
						} else if (which == 1) {
							choice = "�ʾ�";
						} else if (which == 2) {
							choice = "ҩ��";
						} else if (which == 3) {
							choice = "ҽԺ";
						} else if (which == 4) {
							choice = "ͼ���";
						} else if (which == 5) {
							choice = "�ƶ�Ӫҵ��";
						} else if (which == 6) {
							choice = "��ͨӪҵ��";
						} else if (which == 7) {
							choice = "����Ӫҵ��";
						} else if (which == 8) {
							choice = "��������";
						} else if (which == 9) {
							choice = "��Ӱ��ӡ";
						}
						key=choice;	
						System.out.println(key);
//						searchEditText.setText(key);
						handler.sendEmptyMessage(0x0014);
					}
				});
		builder.setPositiveButton("����", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				progress = ProgressDialog.show(SearchActivity.this, "����",
						"��������,���Ե�...", true, true);
				progress.setCanceledOnTouchOutside(false);
				progress.setOnCancelListener(new DialogInterface.OnCancelListener() {

					public void onCancel(DialogInterface dialog) {
						// TODO Auto-generated method stub
						Toast.makeText(SearchActivity.this, "����ʧ��,������������",
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
							poiResult = poiSearch.searchPoiInCity(key,"�人");
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
		builder.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {

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
		builder.setTitle("��ѯ");
		builder.setSingleChoiceItems(new String[] { "��ӰԺ", "KTV", "ϴԡ", "����",
				"̨����", "�ư�", "���", "����" }, 0,
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						if (which == 0) {
							choice = "��ӰԺ";
						} else if (which == 1) {
							choice = "KTV";
						} else if (which == 2) {
							choice = "ϴԡ";
						} else if (which == 3) {
							choice = "����";
						} else if (which == 4) {
							choice = "̨����";
						} else if (which == 5) {
							choice = "�ư�";
						} else if (which == 6) {
							choice = "���";
						} else if (which == 7) {
							choice = "����";
						}
						key=choice;	
						System.out.println(key);
//						searchEditText.setText(key);
						handler.sendEmptyMessage(0x0014);
					}
				});
		builder.setPositiveButton("����", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				progress = ProgressDialog.show(SearchActivity.this, "����",
						"��������,���Ե�...", true, true);
				progress.setCanceledOnTouchOutside(false);
				progress.setOnCancelListener(new DialogInterface.OnCancelListener() {

					public void onCancel(DialogInterface dialog) {
						// TODO Auto-generated method stub
						Toast.makeText(SearchActivity.this, "����ʧ��,������������",
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
							poiResult = poiSearch.searchPoiInCity(key,"�人");
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
		builder.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {

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
		builder.setTitle("��ѯ");
		builder.setSingleChoiceItems(new String[] { "����", "��԰", "����վ", "���վ",
				"����վ" }, 0, new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				if (which == 0) {
					choice = "����";
				} else if (which == 1) {
					choice = "��԰";
				} else if (which == 2) {
					choice = "����վ";
				} else if (which == 3) {
					choice = "���վ";
				} else if (which == 4) {
					choice = "����վ";
				}
				key=choice;	
				System.out.println(key);
//				searchEditText.setText(key);
				handler.sendEmptyMessage(0x0014);
			}
		});
		builder.setPositiveButton("����", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				progress = ProgressDialog.show(SearchActivity.this, "����",
						"��������,���Ե�...", true, true);
				progress.setCanceledOnTouchOutside(false);
				progress.setOnCancelListener(new DialogInterface.OnCancelListener() {

					public void onCancel(DialogInterface dialog) {
						// TODO Auto-generated method stub
						Toast.makeText(SearchActivity.this, "����ʧ��,������������",
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
							poiResult = poiSearch.searchPoiInCity(key,"�人");
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
		builder.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {

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
		builder.setTitle("��ѯ");
		builder.setSingleChoiceItems(new String[] { "����վ", "ͣ����", "����ά��",
				"��������" }, 0, new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				if (which == 0) {
					choice = "����վ";
				} else if (which == 1) {
					choice = "ͣ����";
				} else if (which == 2) {
					choice = "����ά��";
				} else if (which == 3) {
					choice = "��������";
				}
				key=choice;	
				System.out.println(key);
//				searchEditText.setText(key);
				handler.sendEmptyMessage(0x0014);
			}
		});
		builder.setPositiveButton("����", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				progress = ProgressDialog.show(SearchActivity.this, "����",
						"��������,���Ե�...", true, true);
				progress.setCanceledOnTouchOutside(false);
				progress.setOnCancelListener(new DialogInterface.OnCancelListener() {

					public void onCancel(DialogInterface dialog) {
						// TODO Auto-generated method stub
						Toast.makeText(SearchActivity.this, "����ʧ��,������������",
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
							poiResult = poiSearch.searchPoiInCity(key,"�人");
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
		builder.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {

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
		builder.setTitle("��ѯ");
		builder.setSingleChoiceItems(new String[] { "������", "������", "������", "�ֵ���",
				"��Ժ" }, 0, new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				if (which == 0) {
					choice = "������";
				} else if (which == 1) {
					choice = "������";
				} else if (which == 2) {
					choice = "������";
				} else if (which == 3) {
					choice = "�ֵ���";
				} else if (which == 4) {
					choice = "��Ժ";
				}
				key=choice;	
				System.out.println(key);
//				searchEditText.setText(key);
				handler.sendEmptyMessage(0x0014);
			}
		});
		builder.setPositiveButton("����", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				progress = ProgressDialog.show(SearchActivity.this, "����",
						"��������,���Ե�...", true, true);
				progress.setCanceledOnTouchOutside(false);
				progress.setOnCancelListener(new DialogInterface.OnCancelListener() {

					public void onCancel(DialogInterface dialog) {
						// TODO Auto-generated method stub
						Toast.makeText(SearchActivity.this, "����ʧ��,������������",
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
							poiResult = poiSearch.searchPoiInCity(key,"�人");
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
		builder.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		builder.create().show();
	}
	
	
    
}
