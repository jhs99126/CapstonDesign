from transformers import TextClassificationPipeline, ElectraForSequenceClassification, AutoTokenizer, BertForSequenceClassification

# model_name = 'server\electra_param_preprocessing'
model_name = 'smilegate-ai/kor_unsmile'
model = BertForSequenceClassification.from_pretrained(model_name)
tokenizer = AutoTokenizer.from_pretrained(model_name)

pipe = TextClassificationPipeline(
    model=model,
    tokenizer=tokenizer,
    device=-1,     # cpu: -1, gpu: gpu number
    return_all_scores=True,
    # top_k=None,
    function_to_apply='sigmoid'
    )

def calcScore(textline):
    i = 0
    index = 0
    score = 0
    reasonlist = ['여성이나 가족을 비하하는 단어가 포함되어 있어요', '남성을 비하하는 단어가 포함되어 있어요',
            '성소수자를 비하하는 단어가 포함되어 있어요', '인종차별을 하는 단어가 포함되어 있어요', 
            '특정한 연령을 비하하는 단어가 포함되어 있어요', '특정 지역을 비하하는 단어가 포함되어 있어요',
            '특정 종교를 비하하는 단어가 포함되어 있어요', '혐오적인 단어가 포함되어 있어요',
            '악플이나 욕설인 단어가 포함되어 있어요', '깨끗한 문장입니다.']
    text_pipe = pipe(textline)[0]
    for p in text_pipe:
        for key, value in p.items():
            if key == 'score':
                if score < value:
                    score = value
                    index = i
        i += 1  
    is_not_good=False          

    # clean이 30% 이상일 때, 가장 높은 index의 score가 0.6 이하일 때는 good
    if (index !=9) & ((text_pipe[-1]['score']<0.3) | (text_pipe[index]['score']>0.58)):
        is_not_good = True
    return is_not_good,text_pipe,reasonlist[index]

def select_age(num_problem_sentences,score):
    if num_problem_sentences==0:
        return 0
    # clean이 0.73보다 크면 7세
    if score>0.73:
        return 7
    return 12

if __name__=="__main__":
    print(calcScore(input("text 입력: ")))
