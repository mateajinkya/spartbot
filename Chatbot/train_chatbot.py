import spacy
from spacy.training.example import Example
import json

# Load the language model. spaCy comes with pre-trained language models that we must fine-tune. 
nlp = spacy.blank("en")

# Load training data
with open('intents.json') as f:
    intents = json.load(f)

# Add text categorizer to the pipeline if it doesn't exist
if "textcat" not in nlp.pipe_names:
    textcat = nlp.add_pipe("textcat", last=True)
else:
    textcat = nlp.get_pipe("textcat")

# Add labels to the text categorizer
for intent in intents["intents"]:
    textcat.add_label(intent["tag"])

# Prepare training data
train_data = []
for intent in intents["intents"]:
    for pattern in intent["patterns"]:
        doc = nlp.make_doc(pattern)
        example = Example.from_dict(doc, {"cats": {intent["tag"]: 1.0}})
        train_data.append(example)

# Train the model
nlp.begin_training()
for i in range(10):
    losses = {}
    batches = spacy.util.minibatch(train_data, size=spacy.util.compounding(4.0, 32.0, 1.001))
    for batch in batches:
        nlp.update(batch, drop=0.5, losses=losses)
    print(f"Iteration {i}, Losses: {losses}")

# Save the model
nlp.to_disk("chatbot_model")