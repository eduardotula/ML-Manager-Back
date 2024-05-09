ALTER TABLE public.orderm ADD CONSTRAINT order_id_unique_constraint UNIQUE (order_id);
ALTER TABLE public.reclamacao ADD CONSTRAINT reclamacao_id_unique_constraint UNIQUE (reclamacao_id);

