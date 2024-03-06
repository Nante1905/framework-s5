import {
  Button,
  CircularProgress,
  FormControl,
  InputLabel,
  MenuItem,
  Select,
  TextField,
} from "@mui/material";
import axios from "axios";
import qs from "qs";
import { useEffect, useState } from "react";
import { useForm } from "react-hook-form";
import ErrorSnackBar from "../../shared/components/snackbar/ErrorSnackBar";
import SuccessSnackBar from "../../shared/components/snackbar/SuccessSnackBar";
import { URL_API } from "../../shared/env/env";
import { Marque } from "../../shared/types/Marque.ts";

const ProduitForm = (props: ProduitProps) => {
  const [state, setState] = useState<ProduitState>(initialState);
  useEffect(() => {
    setState((state) => ({
      ...state,
      loading: true,
    }));

    axios
      .get(`${URL_API}/marque.do`)
      .then((res) => {
        setState((state) => ({
          ...state,
          loading: false,
          marque: res.data.data,
        }));
      })
      .catch((err) => {
        let errorMessage = "Une erreur s'est produite";
        if (
          err.response?.data.err &&
          err.response?.data.err != "" &&
          err.response?.data.err != null
        ) {
          errorMessage = err.response.data.err;
        }
        setState((state) => ({
          ...state,
          error: errorMessage,
          loading: false,
        }));
      });
  }, []);

  const onSubmit = async (data: any) => {
    setState((state) => ({
      ...state,
      sendLoading: true,
    }));
    let url = `${URL_API}/insertproduit.do`;
    if (props.id) {
      url = `${URL_API}/updateproduit.do`;
    }
    const options = {
      method: "POST",
      headers: { "content-type": "application/x-www-form-urlencoded" },
      data: qs.stringify({
        ...data,
      }),
      url,
    };
    const res = await axios(options);
    console.log(res);
    if (!res.data.data) {
      setState((state) => ({
        ...state,
        error: (res.data as string).includes("Exception")
          ? "Une erreur s'est produite"
          : res.data,
      }));
    } else {
      setState((state) => ({
        ...state,
        success: "Enregistré",
      }));
    }
  };

  const { register, formState, handleSubmit, control } = useForm({
    defaultValues: props.id
      ? async () =>
          (await axios.get(`${URL_API}/produit.do?idproduit=${props.id}`)).data
            .data?.[0]
      : undefined,
  });
  const sleep = (ms: any) => new Promise((resolve) => setTimeout(resolve, ms));

  return (
    <>
      <div className="crud-form-wrapper">
        <h1 className="text-center">
          {props.id ? "Modification" : "Création"} Produit
        </h1>
        {state.loading || formState.isLoading ? (
          <CircularProgress />
        ) : (
          <form
            className="form-content"
            onSubmit={handleSubmit(async (data) => {
              console.log(data);
              await onSubmit(data);
            })}
          >
            <div className="form-input">
              <input type="hidden" {...register("idproduit")} />
            </div>

            <div className="form-input">
              <TextField label="Nom" {...register("nom")} />
            </div>

            <div className="form-input">
              <TextField label="Prix" {...register("prix")} />
            </div>

            <div className="form-input">
              {state.loading ? (
                <CircularProgress />
              ) : (
                <FormControl sx={{ width: 200 }}>
                  <InputLabel id="demo-simple-select-label">Marque</InputLabel>
                  <Select
                    labelId="demo-simple-select-label"
                    id="demo-simple-select"
                    label="Marque"
                    {...register("marque")}
                    defaultValue={
                      formState.defaultValues?.["marque"]?.["idmarque"]
                    }
                  >
                    {state.marque?.map((e: Marque, i) => (
                      <MenuItem key={`fk${i}`} value={e?.idmarque}>
                        {e.label}
                      </MenuItem>
                    ))}
                  </Select>
                </FormControl>
              )}
            </div>

            <div className="form-input">
              <TextField
                label="Disponibilite"
                type="number"
                {...register("disponibilite")}
              />
            </div>

            <div className="inline-flex-center vertical-margin">
              <Button variant="contained" type="submit">
                {formState.isSubmitting ? (
                  <CircularProgress
                    sx={{
                      width: "20px !important",
                      height: "20px !important",
                      color: "white",
                    }}
                  />
                ) : (
                  <>Valider</>
                )}
              </Button>
            </div>
          </form>
        )}
      </div>
      <ErrorSnackBar
        open={state.error !== null}
        onClose={() =>
          setState(() => ({
            ...state,
            error: null,
          }))
        }
        error={state.error as string}
      />
      <SuccessSnackBar
        open={state.success !== null}
        onClose={() =>
          setState(() => ({
            ...state,
            success: null,
          }))
        }
        message={state.success as string}
      />
    </>
  );
};

interface ProduitProps {
  id?: string;
  onClose?: () => void;
}

interface ProduitState {
  loading: boolean;
  sendLoading: boolean;
  error: string | null;
  success: string | null;
  marque: Marque[];
}

const initialState: ProduitState = {
  loading: false,
  sendLoading: false,
  error: null,
  success: null,
  marque: [],
};

export default ProduitForm;

