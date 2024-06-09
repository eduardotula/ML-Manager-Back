ALTER TABLE public.anuncio ADD imposto NUMERIC(18,2);
UPDATE anuncio set imposto = 0.0;
ALTER TABLE anuncio ALTER COLUMN imposto SET NOT NULL;

ALTER TABLE public.venda ADD imposto NUMERIC(18,2);
UPDATE venda set imposto = 0.0;
ALTER TABLE venda ALTER COLUMN imposto SET NOT NULL;
