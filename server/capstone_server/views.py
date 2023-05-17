from django.shortcuts import render
from django.http import HttpResponse,JsonResponse
from django.views.decorators.csrf import csrf_exempt
import sys
from kss import split_sentences
from collections import defaultdict
import json

sys.path.append('../')
from modules import yt,stt,unsmile

article = '''
<!DOCTYPE html>
<html>
  <head>
    <title>Android Studio</title>
  </head>
  <body>
    <h1>UI 구현부분</h1>
    <form method="post" action="/check/">
      <label for="id">URL:</label>
      <input type="text" id="id" name="url">
      <br>
      <input type="submit" value="Submit">
    </form>
  </body>
</html>

    '''
def index(request):
    return HttpResponse(article)

@csrf_exempt
def check(request):
    try:
        url = request.POST['url']
    except:
        print('JSON 데이터')
        data=json.loads(request.body)
        url = data.get('url')
    response = {}

    # yt 부분
    yt_res,yt_content = yt.download_shorts(url)
    if yt_res == True:
        title,audio_file_path=yt_content
        response['result']="y"
        response['title']=title
    else:
        err_msg = yt_content
        return JsonResponse({'result':'n' , "err_msg":err_msg})

    # stt 부분
    stt_id = stt.useAPI(audio_file_path) # stt후 id 반환
    stt_result = stt.makeTextline(stt_id)
    all_text= ''
    for stt_result_text in stt_result['results']['utterances']:
        all_text +=stt_result_text['msg']
    texts = split_sentences(all_text) # 문장 분리

    # unsmile 부분
    unsmile_score = defaultdict(float)
    nps = 0 # num_problem_sentences
    ps = [] # problem_sentences 
    for text in texts:
        is_not_good,text_pipe,reason = unsmile.calcScore(text)
        tmp ={}
        if is_not_good:
            nps += 1
            tmp['sentence']=text
            tmp['reason']=reason
        for t in text_pipe:
            unsmile_score[t['label']]+=t['score']
        if len(tmp)>0:
            ps.append(tmp)
    response['num_problem_sentences'] = nps
    response['problem_sentences'] = ps
    for key in unsmile_score.keys():    #score별 평균 구하기
        unsmile_score[key] /= len(texts)

    response['age'] = unsmile.select_age(nps, unsmile_score['clean'])

    # html에서 한글 깨지는 거 수정
    return JsonResponse(response,json_dumps_params={'ensure_ascii': False}, status=200)