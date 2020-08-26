package com.ambgen.godzgeneralblog.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class Helper {
//    public static String returnCategoryName(int id){
//            if(id==2){
//                return  "Education";
//            }else if(id==12){
//                return "games";
//            }else if(id==3){
//                return  "Health Tips";
//            }else if(id==8){
//                return "Lifestyle";
//            }else if(id==20){
//                return "Mobile Banking";
//            }else if(id==6){
//                return "Network";
//            }else if(id==4){
//                    return "News";
//            }else if(id==7){
//                return "Sprituality";
//            }else if(id==9){
//                return "Sport";
//            }else if(id==5){
//                return "Technology";
//            }else return "Update App";
//
//    }
public static String returnCategoryName(long id){
    if(id==449557068){
        return "Advertising Tech";
    }else if(id==576702152){
        return "Aerospace";
    }else if(id==449557102){
        return "Apps";
    }

    else if(id==424613844){
        return "Artificial Intelligence";
    }
    else if(id==449557111){
        return "Asia";
    }

    else if(id==421926862){
        return "Augmented Reality";
    }
    else if(id==449557096){
        return "Automotive";
    }
    else if(id==576596927){
        return "Biotech";
    }

    else if(id==576602837){
        return "Blockchain";
    }

    else if(id==576604891){
        return "Book Review";
    }

    return "Update App";
}




    public static String getMonth(int month){
        if(month==1){
            return "January";
        }else if(month==2){
            return "February";
        }else if(month==3){
            return "March";
        }else if(month==4){
            return "April";
        }else if(month==5){
            return "May";
        }else if(month==6){
            return "June";
        }else if(month==7){
            return "July";
        }else if(month==8){
            return "August";
        }else if(month==9){
            return "September";
        }else if(month==10){
            return "October";
        }else if(month==11){
            return "November";
        }else if(month==12){
            return "December";
        }else{
            return  "Update App";
        }
    }

    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
